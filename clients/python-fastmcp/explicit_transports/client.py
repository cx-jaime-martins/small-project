"""Demonstrates FastMCP's explicit transport classes (StreamableHttpTransport,
SSETransport, StdioTransport), constructed standalone and passed to Client(...).

The sibling diagnostics/prompts_as_tools samples only exercise the bare-URL /
local-script inference path (Client(url) / Client("script.py")) - this
sample covers the explicit transport classes those don't reach.
"""

import asyncio

from fastmcp import Client
from fastmcp.client.transports import SSETransport, StdioTransport, StreamableHttpTransport


async def main() -> None:
    http_transport = StreamableHttpTransport("https://mcp.example.com/mcp")
    async with Client(http_transport) as client:
        print(await client.list_tools())

    sse_transport = SSETransport("https://mcp.example.com/sse")
    async with Client(sse_transport) as client:
        print(await client.list_tools())

    stdio_transport = StdioTransport("uvx", ["mcp-server-git==1.4.0", "--repository", "/repo"])
    async with Client(stdio_transport) as client:
        print(await client.list_tools())


if __name__ == "__main__":
    asyncio.run(main())
