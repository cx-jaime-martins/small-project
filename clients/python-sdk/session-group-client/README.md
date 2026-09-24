# MCP Python SDK - ClientSessionGroup (Multi-Server) Client

Minimal client demonstrating `mcp.client.session_group.ClientSessionGroup`,
which manages connections to multiple MCP servers concurrently and aggregates
their tools/resources/prompts under one interface.

Each server is described by a `ServerParameters` value - `StdioServerParameters`
(already used elsewhere in this repo), or the newer `SseServerParameters` /
`StreamableHttpParameters` (both just carry a `url`) - and
`ClientSessionGroup.connect_to_server(...)` picks the right transport
internally based on which one was passed.

```python
server_params = [
    StdioServerParameters(command="uvx", args=["mcp-server-git"]),
    SseServerParameters(url="https://mcp.example.com/sse"),
    StreamableHttpParameters(url="https://mcp.example.com/mcp"),
]

async with ClientSessionGroup() as group:
    for params in server_params:
        await group.connect_to_server(params)
```

## Run

```bash
pip install -e .
mcp-session-group-client
```
