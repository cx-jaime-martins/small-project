package io.modelcontextprotocol.sample.server;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.Tool;

/**
 * Minimal MCP server sample built with the Java SDK.
 *
 * <p>
 * Exposes a single {@code calculator} tool over the STDIO transport, mirroring the
 * calculator example used throughout the java-sdk documentation
 * (https://modelcontextprotocol.io/sdk/java/mcp-server). Run it directly, or point an
 * MCP client (e.g. the {@code stdio-quickstart-client} sample) at the packaged jar.
 */
public class CalculatorServer {

	public static void main(String[] args) {
		// STDIO transport: bidirectional JSON-RPC over stdin/stdout.
		var transportProvider = new StdioServerTransportProvider(McpJsonDefaults.getMapper());

		SyncToolSpecification calculatorTool = SyncToolSpecification.builder()
			.tool(Tool.builder("calculator", calculatorSchema()).description(
					"Performs basic arithmetic (add, subtract, multiply, divide) on two numbers.")
				.build())
			.callHandler((exchange, request) -> {
				Map<String, Object> arguments = request.arguments();
				String operation = String.valueOf(arguments.get("operation"));
				double a = ((Number) arguments.get("a")).doubleValue();
				double b = ((Number) arguments.get("b")).doubleValue();

				double result;
				switch (operation) {
					case "add" -> result = a + b;
					case "subtract" -> result = a - b;
					case "multiply" -> result = a * b;
					case "divide" -> {
						if (b == 0) {
							return CallToolResult.builder()
								.addTextContent("Invalid argument: division by zero.")
								.isError(true)
								.build();
						}
						result = a / b;
					}
					default -> {
						return CallToolResult.builder()
							.addTextContent("Unsupported operation: " + operation
									+ ". Use one of add, subtract, multiply, divide.")
							.isError(true)
							.build();
					}
				}

				return CallToolResult.builder().addTextContent("Result: " + result).build();
			})
			.build();

		McpSyncServer server = McpServer.sync(transportProvider)
			.serverInfo("calculator-server", "0.1.0")
			.capabilities(ServerCapabilities.builder().tools(true).build())
			.tools(calculatorTool)
			.build();

		// The transport's inbound/outbound schedulers run on non-daemon threads, so the
		// JVM stays alive to service requests after main() returns. Add a shutdown hook
		// so Ctrl+C (or the parent process closing stdin) still closes the session cleanly.
		Runtime.getRuntime().addShutdownHook(new Thread(server::close));
	}

	private static Map<String, Object> calculatorSchema() {
		Map<String, Object> operation = new LinkedHashMap<>();
		operation.put("type", "string");
		operation.put("enum", List.of("add", "subtract", "multiply", "divide"));
		operation.put("description", "The arithmetic operation to perform.");

		Map<String, Object> number = Map.of("type", "number");

		Map<String, Object> properties = new LinkedHashMap<>();
		properties.put("operation", operation);
		properties.put("a", number);
		properties.put("b", number);

		Map<String, Object> schema = new LinkedHashMap<>();
		schema.put("type", "object");
		schema.put("properties", properties);
		schema.put("required", List.of("operation", "a", "b"));
		return schema;
	}

}