# Streamable HTTP Client

A minimal MCP client built with the [MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk), connecting
to a server over the **Streamable HTTP** transport.

Pairs with the [`streamable-http-weather-server`](../../../servers/java-sdk/streamable-http-weather-server)
sample.

## What it shows

- `HttpClientStreamableHttpTransport.builder(baseUri).endpoint(path)` — the JDK-`HttpClient`-based transport
  included in the core `mcp` module (no external HTTP client dependency needed)
- `McpClient.sync(...)` connecting to an already-running server, as opposed to the STDIO sample which launches
  its own subprocess
- Reading `CallToolResult.isError()` for a tool-level error (an unsupported city)

## Build

```bash
mvn -q package
```

## Run

Start the server first:

```bash
cd ../../../servers/java-sdk/streamable-http-weather-server
mvn -q package
java -jar target/streamable-http-weather-server.jar &
cd -
```

Then run the client:

```bash
java -jar target/streamable-http-client.jar http://localhost:8080/mcp
```

Expected output:

```
Available tools:
 - list_cities: Lists the cities with mock forecast data.
 - get_forecast: Gets the mock weather forecast for a city.

Cities: [TextContent[...text=seattle, austin, denver, miami...]]
get_forecast(Seattle) -> [OK] [TextContent[...text=Seattle: Rainy, 54°F, wind 12 mph...]]
get_forecast(Austin) -> [OK] [TextContent[...text=Austin: Sunny, 89°F, wind 6 mph...]]
get_forecast(Atlantis) -> [ERROR] [TextContent[...text=Unknown city 'Atlantis'. Supported cities: seattle, austin, denver, miami...]]
```