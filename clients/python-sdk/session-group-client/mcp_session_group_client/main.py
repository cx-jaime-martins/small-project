"""Minimal client demonstrating the official MCP Python SDK's ClientSessionGroup
(mcp.client.session_group.ClientSessionGroup), which manages connections to
multiple MCP servers concurrently and aggregates their tools/resources/prompts.

Unlike the sibling streamable-http-client/sse-polling-client samples (which each
connect to a single server directly via streamablehttp_client/sse_client),
ClientSessionGroup.connect_to_server(...) takes a ServerParameters value - a
StdioServerParameters, SseServerParameters, or StreamableHttpParameters - and
handles picking the right transport internally.
"""

import asyncio

from mcp import StdioServerParameters
from mcp.client.session_group import ClientSessionGroup, SseServerParameters, StreamableHttpParameters


async def main() -> None:
    server_params = [
        StdioServerParameters(command="uvx", args=["mcp-server-git"]),
        SseServerParameters(url="https://mcp.example.com/sse"),
        StreamableHttpParameters(url="https://mcp.example.com/mcp"),
    ]

    async with ClientSessionGroup() as group:
        for params in server_params:
            await group.connect_to_server(params)

        print(f"Connected to {len(group.sessions)} server(s)")
        for name in group.tools:
            print(f"  - {name}")


if __name__ == "__main__":
    asyncio.run(main())
