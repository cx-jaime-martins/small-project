package io.modelcontextprotocol.sample.server;

import java.io.File;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.Tool;

/**
 * MCP server sample built with the Java SDK, exposing mock weather tools over the
 * Streamable HTTP transport.
 *
 * <p>
 * Unlike the STDIO transport, {@code HttpServletStreamableServerTransportProvider} is a
 * plain {@code jakarta.servlet.http.HttpServlet}: the java-sdk ships it with a
 * {@code provided}-scope dependency on the servlet API and leaves hosting it to the
 * application. This sample hosts it in an embedded Tomcat instance, the same approach
 * the SDK's own conformance test server uses.
 */
public class WeatherServer {

	private static final Map<String, WeatherInfo> FORECASTS = buildForecasts();

	public static void main(String[] args) throws Exception {
		int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
		String mcpEndpoint = "/mcp";

		HttpServletStreamableServerTransportProvider transportProvider = HttpServletStreamableServerTransportProvider
			.builder()
			.jsonMapper(McpJsonDefaults.getMapper())
			.mcpEndpoint(mcpEndpoint)
			.build();

		McpSyncServer server = McpServer.sync(transportProvider)
			.serverInfo("weather-server", "0.1.0")
			.capabilities(ServerCapabilities.builder().tools(true).build())
			.tools(listCitiesTool(), getForecastTool())
			.build();

		Tomcat tomcat = startTomcat(port, mcpEndpoint, transportProvider);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			server.close();
			try {
				tomcat.stop();
			}
			catch (Exception ignored) {
				// best effort on shutdown
			}
		}));

		System.out.println("Weather MCP server listening on http://localhost:" + port + mcpEndpoint);
		tomcat.getServer().await();
	}

	private static Tomcat startTomcat(int port, String mcpEndpoint,
			HttpServletStreamableServerTransportProvider transportProvider) throws Exception {
		File baseDir = Files.createTempDirectory("mcp-weather-server-").toFile();
		baseDir.deleteOnExit();

		Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir(baseDir.getAbsolutePath());
		tomcat.setPort(port);
		tomcat.getConnector(); // force connector creation before adding the context

		Context context = tomcat.addContext("", baseDir.getAbsolutePath());
		Wrapper wrapper = context.createWrapper();
		wrapper.setName("mcpServlet");
		wrapper.setServlet(transportProvider);
		wrapper.setAsyncSupported(true);
		context.addChild(wrapper);
		context.addServletMappingDecoded("/*", "mcpServlet");

		tomcat.start();
		return tomcat;
	}

	private static SyncToolSpecification listCitiesTool() {
		Map<String, Object> schema = Map.of("type", "object", "properties", Map.of());
		return SyncToolSpecification.builder()
			.tool(Tool.builder("list_cities", schema).description("Lists the cities with mock forecast data.").build())
			.callHandler((exchange, request) -> CallToolResult.builder()
				.addTextContent(String.join(", ", FORECASTS.keySet()))
				.build())
			.build();
	}

	private static SyncToolSpecification getForecastTool() {
		Map<String, Object> properties = Map.of("city", Map.of("type", "string",
				"description", "City name, e.g. 'Seattle'. See the list_cities tool for supported values."));
		Map<String, Object> schema = new LinkedHashMap<>();
		schema.put("type", "object");
		schema.put("properties", properties);
		schema.put("required", List.of("city"));

		return SyncToolSpecification.builder()
			.tool(Tool.builder("get_forecast", schema).description("Gets the mock weather forecast for a city.").build())
			.callHandler((exchange, request) -> {
				String city = String.valueOf(request.arguments().get("city"));
				WeatherInfo forecast = FORECASTS.get(normalize(city));
				if (forecast == null) {
					return CallToolResult.builder()
						.addTextContent("Unknown city '" + city + "'. Supported cities: "
								+ FORECASTS.keySet().stream().collect(Collectors.joining(", ")))
						.isError(true)
						.build();
				}
				return CallToolResult.builder()
					.addTextContent(String.format(Locale.US, "%s: %s, %d°F, wind %d mph", city,
							forecast.conditions(), forecast.temperatureF(), forecast.windMph()))
					.build();
			})
			.build();
	}

	private static Map<String, WeatherInfo> buildForecasts() {
		Map<String, WeatherInfo> forecasts = new LinkedHashMap<>();
		forecasts.put("seattle", new WeatherInfo("Rainy", 54, 12));
		forecasts.put("austin", new WeatherInfo("Sunny", 89, 6));
		forecasts.put("denver", new WeatherInfo("Windy", 61, 22));
		forecasts.put("miami", new WeatherInfo("Humid", 85, 9));
		return forecasts;
	}

	private static String normalize(String city) {
		return city == null ? "" : city.trim().toLowerCase(Locale.US);
	}

	private record WeatherInfo(String conditions, int temperatureF, int windMph) {
	}

}