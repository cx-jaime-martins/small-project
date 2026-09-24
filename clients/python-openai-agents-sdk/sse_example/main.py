"""Minimal client demonstrating the OpenAI Agents SDK's MCPServerSse,
connecting to a remote MCP server over a literal URL.

Unlike the official filesystem_example/sse_example samples upstream (which
derive the URL from an env var / dynamically chosen local port), this uses a
fixed remote endpoint to keep the example self-contained.
"""

import asyncio

from agents import Agent, Runner
from agents.mcp import MCPServer, MCPServerSse


async def run(mcp_server: MCPServer):
    agent = Agent(
        name="Assistant",
        instructions="Use the tools to answer the questions.",
        mcp_servers=[mcp_server],
    )

    result = await Runner.run(starting_agent=agent, input="What tools do you have access to?")
    print(result.final_output)


async def main():
    async with MCPServerSse(
        name="Remote SSE Server",
        params={"url": "https://mcp.example.com/sse"},
    ) as server:
        await run(server)


if __name__ == "__main__":
    asyncio.run(main())
