# openai-agents-mcp-examples

Examples of the [OpenAI Agents SDK](https://github.com/openai/openai-agents-python)
(`openai-agents`, imported as `agents`) connecting to external MCP servers.

- `filesystem_example/main.py` — `MCPServerStdio` launching the
  `@modelcontextprotocol/server-filesystem` npm package via `npx` (copied
  verbatim from the SDK's own
  [`examples/mcp/filesystem_example`](https://github.com/openai/openai-agents-python/blob/main/examples/mcp/filesystem_example/main.py)).
- `sse_example/main.py` — `MCPServerSse` connecting to a remote MCP server
  over a literal URL (minimal, hand-written — the upstream SSE examples
  derive the URL dynamically, which isn't useful for exercising static
  detection).

## Run

```bash
uv run --with openai-agents filesystem_example/main.py
uv run --with openai-agents sse_example/main.py
```
