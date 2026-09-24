package com.example.mcp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MCP server sample built with the Spring AI MCP Server WebMVC Boot Starter, exposing
 * mock weather tools over the Streamable HTTP transport.
 *
 * <p>
 * Unlike the {@code stdio-calculator-server} sample, this one is a regular Spring MVC
 * web application: {@code spring-ai-starter-mcp-server-webmvc} brings in
 * {@code spring-boot-starter-web} and registers the MCP endpoint (default {@code /mcp})
 * as a servlet, so no embedded-container wiring is needed in application code.
 */
@SpringBootApplication
public class StreamableHttpWeatherServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamableHttpWeatherServerApplication.class, args);
	}

}
