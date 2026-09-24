# Streamable HTTP Weather Server

An MCP server built with the [MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk), exposing mock
weather tools over the **Streamable HTTP** transport, hosted in an embedded Tomcat server.

## What it shows

- `HttpServletStreamableServerTransportProvider` — the SDK ships this as a plain `HttpServlet`
  (`jakarta.servlet-api` is a `provided`-scope dependency of `mcp-core`), so the application must bring its own
  servlet container. This sample embeds Tomcat directly in `main()`, the same approach used by the java-sdk's
  own conformance test server (`conformance-tests/server-servlet`) — no Spring required.
- `McpServer.sync(streamableTransportProvider)` for a synchronous server over a multi-client HTTP transport
- Two simple tools (`list_cities`, `get_forecast`) backed by an in-memory mock dataset, so the sample has no
  external network dependency

## Tools

| Tool            | Description                                      |
|-----------------|---------------------------------------------------|
| `list_cities`    | Lists the cities with mock forecast data          |
| `get_forecast`   | Gets the mock forecast for a city (`city` arg)    |

## Build

```bash
mvn -q package
```

## Run

```bash
java -jar target/streamable-http-weather-server.jar [port]
```

Defaults to port `8080`. The server listens at `http://localhost:8080/mcp`.

Try it with the [`streamable-http-client`](../../../clients/java-sdk/streamable-http-client) sample:

```bash
cd ../../../clients/java-sdk/streamable-http-client
mvn -q package
java -jar target/streamable-http-client.jar http://localhost:8080/mcp
```