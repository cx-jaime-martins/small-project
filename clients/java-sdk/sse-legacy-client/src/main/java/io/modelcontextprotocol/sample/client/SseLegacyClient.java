package io.modelcontextprotocol.sample.client;

import java.time.Duration;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

/**
 * Minimal MCP client sample built with the Java SDK's legacy (framework-agnostic)
 * SSE transport.
 *
 * <p>
 * {@code HttpClientSseClientTransport.builder(...)} is the pre-Streamable-HTTP way of
 * reaching a remote MCP server; still shipped in the core {@code mcp} module for
 * servers that haven't migrated off SSE yet.
 */
public class SseLegacyClient {

	public static void main(String[] args) {
		HttpClientSseClientTransport transport = HttpClientSseClientTransport.builder("https://mcp.example.com")
			.build();

		McpSyncClient client = McpClient.sync(transport).requestTimeout(Duration.ofSeconds(10)).build();

		try {
			client.initialize();

			ListToolsResult tools = client.listTools();
			System.out.println("Available tools:");
			tools.tools().forEach(tool -> System.out.println(" - " + tool.name() + ": " + tool.description()));
		}
		finally {
			client.closeGracefully();
		}
	}

}
