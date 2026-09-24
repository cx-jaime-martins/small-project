"""Minimal client demonstrating the OpenAI Agents SDK's MCPServerStreamableHttp,
connecting to a remote MCP server over a literal URL.

Unlike the sibling filesystem_example (stdio) and sse_example (SSE) samples,
this covers the third server type - Streamable HTTP, the SDK's currently
recommended remote transport.
"""

import asyncio

from agents import Agent, Runner
from agents.mcp import MCPServer, MCPServerStreamableHttp


async def run(mcp_server: MCPServer):
    agent = Agent(
        name="Assistant",
        instructions="Use the tools to answer the questions.",
        mcp_servers=[mcp_server],
    )

    result = await Runner.run(starting_agent=agent, input="What tools do you have access to?")
    print(result.final_output)


async def main():
    async with MCPServerStreamableHttp(
        name="Remote Streamable HTTP Server",
        params={"url": "https://mcp.example.com/mcp"},
    ) as server:
        await run(server)


if __name__ == "__main__":
    asyncio.run(main())
