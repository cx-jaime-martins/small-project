package com.example.mcp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Minimal MCP server sample built with the Spring AI MCP Server Boot Starter.
 *
 * <p>
 * Exposes a {@code calculator} tool set over the STDIO transport. All that's needed is
 * the {@code spring-ai-starter-mcp-server} dependency on the classpath, {@code
 * spring.ai.mcp.server.stdio=true} in configuration, and a {@code @Component} with
 * {@code @McpTool}-annotated methods (see {@link CalculatorTools}) — the auto-configuration
 * takes care of wiring up the {@code McpSyncServer} and the STDIO transport.
 */
@SpringBootApplication
public class StdioCalculatorServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StdioCalculatorServerApplication.class, args);
	}

}
