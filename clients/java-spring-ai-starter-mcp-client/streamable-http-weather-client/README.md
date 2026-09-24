# Streamable HTTP Weather Client

A minimal MCP client built with the [Spring AI MCP Client Boot Starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html),
connecting to a server over the **Streamable HTTP** transport.

Pairs with the [`streamable-http-weather-server`](../../../servers/java-spring-ai-starter-mcp-server/streamable-http-weather-server)
sample.

## What it shows

- `spring-ai-starter-mcp-client` — auto-configures an `McpSyncClient` per named connection declared under
  `spring.ai.mcp.client.streamable-http.connections.*`, using the JDK-`HttpClient`-based transport, to
  connect to an already-running server (as opposed to the STDIO sample, which launches its own subprocess)
- Injecting `List<McpSyncClient>` and calling `listTools()` / `callTool()` directly
- Reading `CallToolResult.isError()` for a tool-level error (an unsupported city)

> For production deployments, Spring AI recommends the WebFlux-based transport
> (`spring-ai-starter-mcp-client-webflux`) instead — swap the dependency and the rest of this sample is
> unchanged.

## Build

```bash
mvn -q package
```

## Run

Start the server first:

```bash
cd ../../../servers/java-spring-ai-starter-mcp-server/streamable-http-weather-server
mvn -q package
java -jar target/streamable-http-weather-server.jar &
cd -
```

Then run the client:

```bash
java -jar target/streamable-http-weather-client.jar
```

Expected output (interleaved with Spring Boot's own startup logging):

```
Available tools:
 - get_forecast: Gets the mock weather forecast for a city.
 - list_cities: Lists the cities with mock forecast data.

Cities: [TextContent[annotations=null, text=seattle, austin, denver, miami, meta=null]]
get_forecast(Seattle) -> [OK] [TextContent[annotations=null, text=Seattle: Rainy, 54°F, wind 12 mph, meta=null]]
get_forecast(Austin) -> [OK] [TextContent[annotations=null, text=Austin: Sunny, 89°F, wind 6 mph, meta=null]]
get_forecast(Atlantis) -> [ERROR] [TextContent[annotations=null, text=Error invoking method: getForecast
Unknown city 'Atlantis'. Supported cities: seattle, austin, denver, miami, meta=null]]
```

(Verified end-to-end with `mvn clean package` and running both jars on JDK 26 / Spring Boot 4.1.0 / Spring AI 2.0.0. On a Windows console with a non-UTF-8 code page, the `°` character may render as `�` — that's a terminal display quirk, not a bug in the sample.)
