package com.example.mcp.client;

import java.util.List;
import java.util.Map;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Minimal MCP client sample built with the Spring AI MCP Client Boot Starter.
 *
 * <p>
 * Unlike the STDIO sample, this client connects to an already-running server rather
 * than launching one: the connection declared under
 * {@code spring.ai.mcp.client.streamable-http.connections.weather} in
 * {@code application.yml} is auto-configured into a ready-to-use {@link McpSyncClient}
 * bean.
 *
 * <p>
 * Pairs with the
 * {@code servers/java-spring-ai-starter-mcp-server/streamable-http-weather-server}
 * sample.
 */
@SpringBootApplication
public class StreamableHttpWeatherClientApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication
			.run(StreamableHttpWeatherClientApplication.class, args);
		System.exit(SpringApplication.exit(context, () -> 0));
	}

	@Bean
	ApplicationRunner runner(List<McpSyncClient> mcpSyncClients) {
		return (ApplicationArguments args) -> {
			for (McpSyncClient client : mcpSyncClients) {
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
		};
	}

}
