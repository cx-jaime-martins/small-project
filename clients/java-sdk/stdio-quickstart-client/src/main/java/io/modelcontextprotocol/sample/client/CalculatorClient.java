package io.modelcontextprotocol.sample.client;

import java.time.Duration;
import java.util.Map;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

/**
 * Minimal MCP client sample built with the Java SDK.
 *
 * <p>
 * Launches the {@code stdio-quickstart-server} sample as a subprocess over the STDIO
 * transport, lists its tools, and calls the {@code calculator} tool a few times.
 */
public class CalculatorClient {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println(
					"Usage: java -jar stdio-quickstart-client.jar <path-to-stdio-quickstart-server.jar>");
			System.exit(1);
		}
		String serverJarPath = args[0];

		// STDIO transport: the SDK spawns and owns the server subprocess.
		ServerParameters serverParameters = ServerParameters.builder("java").args("-jar", serverJarPath).build();
		StdioClientTransport transport = new StdioClientTransport(serverParameters, McpJsonDefaults.getMapper());

		McpSyncClient client = McpClient.sync(transport).requestTimeout(Duration.ofSeconds(10)).build();

		try {
			client.initialize();

			ListToolsResult tools = client.listTools();
			System.out.println("Available tools:");
			tools.tools().forEach(tool -> System.out.println(" - " + tool.name() + ": " + tool.description()));

			callCalculator(client, "add", 2, 3);
			callCalculator(client, "multiply", 6, 7);
			callCalculator(client, "divide", 10, 0); // demonstrates a tool-level error
		}
		finally {
			client.closeGracefully();
		}
	}

	private static void callCalculator(McpSyncClient client, String operation, double a, double b) {
		CallToolResult result = client.callTool(
				CallToolRequest.builder("calculator").arguments(Map.of("operation", operation, "a", a, "b", b)).build());

		String status = Boolean.TRUE.equals(result.isError()) ? "ERROR" : "OK";
		System.out.printf("%s(%s, %s) -> [%s] %s%n", operation, a, b, status, result.content());
	}

}