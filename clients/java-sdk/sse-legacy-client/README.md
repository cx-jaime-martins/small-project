# MCP Java SDK - SSE (Legacy) Client

Minimal MCP client demonstrating the Java SDK's legacy, framework-agnostic SSE
transport (`io.modelcontextprotocol.client.transport.HttpClientSseClientTransport`),
included in the core `mcp` module (no Spring/WebFlux dependency required).

Unlike the sibling `streamable-http-client` sample, this connects over the
older SSE transport that predates Streamable HTTP.

```java
HttpClientSseClientTransport transport = HttpClientSseClientTransport
    .builder("https://mcp.example.com")
    .build();
```

## Run

```bash
mvn -q -f pom.xml package
java -jar target/sse-legacy-client.jar
```
