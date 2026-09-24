# STDIO Quickstart Server

A minimal MCP server built with the [MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk), exposing a
single `calculator` tool over the STDIO transport.

## What it shows

- `McpServer.sync(...)` builder for a synchronous, single-session server
- `StdioServerTransportProvider` for process-based (stdin/stdout) transport
- `SyncToolSpecification` with a hand-written JSON Schema (as a `Map<String, Object>`) and a `callHandler`
- Returning tool-level errors via `CallToolResult.builder().isError(true)` (e.g. division by zero) instead of
  throwing, so the calling LLM can see the error and retry

## Tools

| Tool         | Description                                            |
|--------------|---------------------------------------------------------|
| `calculator` | Performs `add`, `subtract`, `multiply`, `divide` on `a` and `b` |

## Build

```bash
mvn -q package
```

This produces a runnable, dependency-shaded jar at `target/stdio-quickstart-server.jar`.

## Run

The server communicates over stdin/stdout, so it's meant to be launched by an MCP client rather than run
interactively. To try it manually:

```bash
java -jar target/stdio-quickstart-server.jar
```

then type a JSON-RPC `initialize` request on stdin, or — much easier — point the
[`stdio-quickstart-client`](../../../clients/java-sdk/stdio-quickstart-client) sample at this jar:

```bash
cd ../../../clients/java-sdk/stdio-quickstart-client
mvn -q package
java -jar target/stdio-quickstart-client.jar ../../../servers/java-sdk/stdio-quickstart-server/target/stdio-quickstart-server.jar
```

Any MCP client configuration (Claude Desktop, etc.) can also launch it directly:

```json
{
  "mcpServers": {
    "calculator": {
      "command": "java",
      "args": ["-jar", "/absolute/path/to/stdio-quickstart-server.jar"]
    }
  }
}
```