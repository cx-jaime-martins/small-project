# Streamable HTTP Weather Server

An MCP server built with the [Spring AI MCP Server WebMVC Boot Starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-server-boot-starter-docs.html),
exposing mock weather tools over the **Streamable HTTP** transport.

## What it shows

- `spring-ai-starter-mcp-server-webmvc` — auto-configures an MCP server on top of Spring MVC, registering
  the MCP endpoint as a servlet; no manual embedded-container setup is needed (contrast with the
  [java-sdk `streamable-http-weather-server`](../../java-sdk/streamable-http-weather-server) sample, which
  hosts the transport in a hand-wired embedded Tomcat)
- `spring.ai.mcp.server.protocol=STREAMABLE` to select the Streamable-HTTP transport (the successor to SSE,
  which is deprecated since Spring AI 2.0.0)
- `@McpTool` / `@McpToolParam` annotations for declaring tools (see `WeatherTools`), backed by an in-memory
  mock dataset so the sample has no external network dependency
- Tool-level errors via a thrown exception (`get_forecast` for an unsupported city), which the starter turns
  into a `CallToolResult` with `isError(true)`

## Tools

| Tool           | Description                                    |
|----------------|-------------------------------------------------|
| `list_cities`  | Lists the cities with mock forecast data        |
| `get_forecast` | Gets the mock forecast for a city (`city` arg)  |

## Build

```bash
mvn -q package
```

## Run

```bash
java -jar target/streamable-http-weather-server.jar
```

The server listens at `http://localhost:8080/mcp` (override the port with `--server.port=<port>`).

Try it with the [`streamable-http-weather-client`](../../../clients/java-spring-ai-starter-mcp-client/streamable-http-weather-client)
sample:

```bash
cd ../../../clients/java-spring-ai-starter-mcp-client/streamable-http-weather-client
mvn -q package
java -jar target/streamable-http-weather-client.jar
```
