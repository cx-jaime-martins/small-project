package com.example.mcp.server;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

/**
 * Calculator tools exposed over MCP.
 *
 * <p>
 * The {@code @McpTool} annotation-scanner (enabled by default) detects this bean and
 * registers each annotated method as an MCP tool, deriving its JSON Schema from the
 * method signature and the {@code @McpToolParam} descriptions. Throwing from a tool
 * method (see {@link #divide(double, double)}) is automatically turned into a
 * tool-level {@code CallToolResult} with {@code isError=true}, so the calling LLM can
 * see the error and retry, instead of the request failing outright.
 */
@Component
public class CalculatorTools {

	@McpTool(name = "add", description = "Add two numbers together")
	public double add(@McpToolParam(description = "First number", required = true) double a,
			@McpToolParam(description = "Second number", required = true) double b) {
		return a + b;
	}

	@McpTool(name = "subtract", description = "Subtract the second number from the first")
	public double subtract(@McpToolParam(description = "First number", required = true) double a,
			@McpToolParam(description = "Second number", required = true) double b) {
		return a - b;
	}

	@McpTool(name = "multiply", description = "Multiply two numbers together")
	public double multiply(@McpToolParam(description = "First number", required = true) double a,
			@McpToolParam(description = "Second number", required = true) double b) {
		return a * b;
	}

	@McpTool(name = "divide", description = "Divide the first number by the second")
	public double divide(@McpToolParam(description = "Dividend", required = true) double a,
			@McpToolParam(description = "Divisor", required = true) double b) {
		if (b == 0) {
			throw new IllegalArgumentException("Invalid argument: division by zero.");
		}
		return a / b;
	}

}
