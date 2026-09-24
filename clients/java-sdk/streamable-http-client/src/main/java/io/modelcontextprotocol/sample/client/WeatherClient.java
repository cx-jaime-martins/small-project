package io.modelcontextprotocol.sample.client;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

/**
 * Minimal MCP client sample built with the Java SDK.
 *
 * <p>
 * Connects to a running server over the Streamable HTTP transport, lists its tools,
 * and calls them. Pairs with the {@code streamable-http-weather-server} sample.
 */
public class WeatherClient {

	public static void main(String[] args) {
		String mcpUrl = args.length > 0 ? args[0] : "http://localhost:8080/mcp";
		URI uri = URI.create(mcpUrl);
		String baseUri = uri.getScheme() + "://" + uri.getAuthority();
		String endpoint = uri.getPath().isEmpty() ? "/mcp" : uri.getPath();

		HttpClientStreamableHttpTransport transport = HttpClientStreamableHttpTransport.builder(baseUri)
			.endpoint(endpoint)
			.build();

		McpSyncClient client = McpClient.sync(transport).requestTimeout(Duration.ofSeconds(10)).build();

		try {
			client.initialize();

			ListToolsResult tools = client.listTools();
			System.out.println("Available tools:");
			tools.tools().forEach(tool -> System.out.println(" - " + tool.name() + ": " + tool.description()));

			CallToolResult cities = client.callTool(CallToolRequest.builder("list_cities").build());
			System.out.println("\nCities: " + cities.content());

			for (String city : new String[] { "Seattle", "Austin", "Atlantis" }) {
				CallToolResult forecast = client
					.callTool(CallToolRequest.builder("get_forecast").arguments(Map.of("city", city)).build());
				String status = Boolean.TRUE.equals(forecast.isError()) ? "ERROR" : "OK";
				System.out.printf("get_forecast(%s) -> [%s] %s%n", city, status, forecast.content());
			}
		}
		finally {
			client.closeGracefully();
		}
	}

}