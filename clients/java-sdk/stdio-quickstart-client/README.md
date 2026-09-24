# STDIO Quickstart Client

A minimal MCP client built with the [MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk). Launches an
MCP server subprocess over the STDIO transport, lists its tools, and calls one.

Pairs with the [`stdio-quickstart-server`](../../../servers/java-sdk/stdio-quickstart-server) sample.

## What it shows

- `McpClient.sync(...)` builder for a synchronous client
- `ServerParameters` + `StdioClientTransport` to launch and own a server subprocess
- `listTools()` / `callTool()` round trips, including reading `CallToolResult.isError()`

## Build

```bash
mvn -q package
```

## Run

First build the server jar it will launch:

```bash
cd ../../../servers/java-sdk/stdio-quickstart-server
mvn -q package
cd -
```

Then run the client, passing the path to the server jar:

```bash
java -jar target/stdio-quickstart-client.jar \
  ../../../servers/java-sdk/stdio-quickstart-server/target/stdio-quickstart-server.jar
```

Expected output:

```
Available tools:
 - calculator: Performs basic arithmetic (add, subtract, multiply, divide) on two numbers.
add(2.0, 3.0) -> [OK] [TextContent[...text=Result: 5.0...]]
multiply(6.0, 7.0) -> [OK] [TextContent[...text=Result: 42.0...]]
divide(10.0, 0.0) -> [ERROR] [TextContent[...text=Invalid argument: division by zero....]]
```