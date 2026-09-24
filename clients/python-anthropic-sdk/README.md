# anthropic-mcp-connector-examples

Examples of the [Anthropic Messages API's MCP connector](https://docs.anthropic.com/en/docs/agents-and-tools/mcp-connector)
(`anthropic`), which lets a `client.beta.messages.create(...)` call reference
external MCP servers directly via the `mcp_servers` request parameter — no
separate MCP client SDK is involved.

- `mcp_connector_example/main.py` — a single external server (Stripe).
- `mcp_connector_example/multi_server.py` — two external servers in one
  `mcp_servers` list (Stripe + GitHub).

## Run

```bash
uv run --with anthropic mcp_connector_example/main.py
uv run --with anthropic mcp_connector_example/multi_server.py
```
