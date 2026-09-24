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
 * The {@code spring-ai-starter-mcp-client} auto-configuration reads the STDIO
 * connection declared in {@code application.yml} under
 * {@code spring.ai.mcp.client.stdio.connections.calculator}, launches the
 * {@code stdio-calculator-server} sample as a subprocess, and registers a ready-to-use
 * {@link McpSyncClient} bean for it — no manual {@code ServerParameters} /
 * {@code StdioClientTransport} wiring required.
 *
 * <p>
 * Pairs with the
 * {@code servers/java-spring-ai-starter-mcp-server/stdio-calculator-server} sample.
 */
@SpringBootApplication
public class StdioCalculatorClientApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(StdioCalculatorClientApplication.class, args);
		// This is a one-shot console app, not a long-running service: exit once the
		// ApplicationRunner below has finished, closing the client (and its server
		// subprocess) gracefully via SpringApplication.exit's context shutdown.
		System.exit(SpringApplication.exit(context, () -> 0));
	}

	@Bean
	ApplicationRunner runner(List<McpSyncClient> mcpSyncClients) {
		return (ApplicationArguments args) -> {
			for (McpSyncClient client : mcpSyncClients) {
				ListToolsResult tools = client.listTools();
				System.out.println("Available tools:");
				tools.tools().forEach(tool -> System.out.println(" - " + tool.name() + ": " + tool.description()));

				callCalculator(client, "add", 2, 3);
				callCalculator(client, "multiply", 6, 7);
				callCalculator(client, "divide", 10, 0); // demonstrates a tool-level error
			}
		};
	}

	private static void callCalculator(McpSyncClient client, String tool, double a, double b) {
		CallToolResult result = client
			.callTool(CallToolRequest.builder(tool).arguments(Map.of("a", a, "b", b)).build());
		String status = Boolean.TRUE.equals(result.isError()) ? "ERROR" : "OK";
		System.out.printf("%s(%s, %s) -> [%s] %s%n", tool, a, b, status, result.content());
	}

}
