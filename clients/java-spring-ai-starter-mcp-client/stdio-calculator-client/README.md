# STDIO Calculator Client

A minimal MCP client built with the [Spring AI MCP Client Boot Starter](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html).
Launches an MCP server subprocess over the STDIO transport, lists its tools, and calls them.

Pairs with the [`stdio-calculator-server`](../../../servers/java-spring-ai-starter-mcp-server/stdio-calculator-server)
sample.

## What it shows

- `spring-ai-starter-mcp-client` — auto-configures an `McpSyncClient` per named connection declared under
  `spring.ai.mcp.client.stdio.connections.*`, launching and owning the server subprocess for you
- Injecting `List<McpSyncClient>` to reach every configured connection, then calling `listTools()` /
  `callTool()` directly (no `ChatModel` / LLM API key needed for this sample — see the note below if you
  want to drive the tools from a `ChatClient` instead)
- Reading `CallToolResult.isError()` for a tool-level error (division by zero)
- A one-shot console app pattern: an `ApplicationRunner` bean does the work, then `main()` calls
  `SpringApplication.exit(...)` so the process terminates instead of staying up like a server

## Using it with a ChatClient instead

This sample calls tools directly for clarity. To let an LLM pick and invoke them instead, inject the
auto-configured `SyncMcpToolCallbackProvider` and pass its callbacks to a `ChatClient`:

```java
ChatClient.create(chatModel)
    .prompt("What is 6 times 7?")
    .tools(toolCallbackProvider.getToolCallbacks())
    .call()
    .content();
```

## Build

```bash
mvn -q package
```

## Run

First build the server jar this client launches:

```bash
cd ../../../servers/java-spring-ai-starter-mcp-server/stdio-calculator-server
mvn -q package
cd -
```

Then run the client (from this directory, so the relative server jar path in `application.yml` resolves):

```bash
java -jar target/stdio-calculator-client.jar
```

Expected output (interleaved with Spring Boot's own startup logging):

```
Available tools:
 - add: Add two numbers together
 - divide: Divide the first number by the second
 - multiply: Multiply two numbers together
 - subtract: Subtract the second number from the first
add(2.0, 3.0) -> [OK] [TextContent[annotations=null, text=5.0, meta=null]]
multiply(6.0, 7.0) -> [OK] [TextContent[annotations=null, text=42.0, meta=null]]
divide(10.0, 0.0) -> [ERROR] [TextContent[annotations=null, text=Error invoking method: divide
Invalid argument: division by zero., meta=null]]
```

(Verified end-to-end with `mvn clean package` and running both jars on JDK 26 / Spring Boot 4.1.0 / Spring AI 2.0.0.)
