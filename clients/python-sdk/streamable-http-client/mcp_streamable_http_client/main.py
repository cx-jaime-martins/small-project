"""Minimal client demonstrating the official MCP Python SDK's Streamable HTTP
transport (mcp.client.streamable_http.streamablehttp_client).

Unlike the sibling sse-polling-client sample (which uses the deprecated SSE
transport), this connects to a remote MCP server over the current
recommended transport and calls its tools.
"""

import asyncio

from mcp import ClientSession
from mcp.client.streamable_http import streamablehttp_client


async def main() -> None:
    async with streamablehttp_client("https://mcp.example.com/mcp") as (read, write, get_session_id):
        async with ClientSession(read, write) as session:
            await session.initialize()

            tools = await session.list_tools()
            print(f"Connected (session {get_session_id()})")
            for tool in tools.tools:
                print(f"  - {tool.name}")


if __name__ == "__main__":
    asyncio.run(main())
