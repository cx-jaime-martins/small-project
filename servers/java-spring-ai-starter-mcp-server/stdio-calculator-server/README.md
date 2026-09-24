# STDIO Calculator Server

A minimal MCP server built with the [Spring AI MCP Server Boot Starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html),
exposing a `calculator` tool set over the STDIO transport.

## What it shows

- `spring-ai-starter-mcp-server` — the auto-configuration that wires up an `McpSyncServer` and the STDIO
  transport from a handful of properties, no manual `McpServer.sync(...)` builder required
- `@McpTool` / `@McpToolParam` annotations for declaring tools (see `CalculatorTools`) — the annotation
  scanner (enabled by default) detects the `@Component` bean and derives the JSON Schema from the method
  signature
- Returning tool-level errors by simply throwing (`divide` throws on division by zero) — the starter turns
  this into a `CallToolResult` with `isError(true)` so the calling LLM can see the error and retry
- Disabling console logging via `logback-spring.xml` — required for STDIO servers, since stdout is
  exclusively the JSON-RPC channel and any stray log line would corrupt it

## Tools

| Tool       | Description                              |
|------------|-------------------------------------------|
| `add`      | Adds two numbers                          |
| `subtract` | Subtracts the second number from the first |
| `multiply` | Multiplies two numbers                    |
| `divide`   | Divides the first number by the second (errors on division by zero) |

## Build

```bash
mvn -q package
```

This produces an executable Spring Boot jar at `target/stdio-calculator-server.jar`.

## Run

The server communicates over stdin/stdout, so it's meant to be launched by an MCP client rather than run
interactively. Point the [`stdio-calculator-client`](../../../clients/java-spring-ai-starter-mcp-client/stdio-calculator-client)
sample at this jar:

```bash
cd ../../../clients/java-spring-ai-starter-mcp-client/stdio-calculator-client
mvn -q package
java -jar target/stdio-calculator-client.jar
```

Any MCP client configuration (Claude Desktop, etc.) can also launch it directly:

```json
{
  "mcpServers": {
    "calculator": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/stdio-calculator-server.jar"]
    }
  }
}
```
