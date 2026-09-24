#!/usr/bin/env python3
"""
SSE MCP Client
Connects to MCP servers via HTTP using Server-Sent Events (SSE) transport.
Alternative to stdio transport for remote/web-based servers.
"""

import asyncio
from typing import Optional
from mcp.client.session import ClientSession
from mcp.client.sse import sse_client


class SSEMCPClient:
    """MCP client using SSE (HTTP) transport instead of stdio."""
    
    def __init__(self, url: str):
        """
        Initialize SSE client.
        
        Args:
            url: HTTP endpoint URL (e.g., "http://localhost:8000/sse")
        """
        self.url = url
        self.client = None
        self.session: Optional[ClientSession] = None
    
    async def connect(self):
        """Connect to the SSE server."""
        print(f"🔌 Connecting to SSE server: {self.url}")
        
        # Create SSE client (HTTP-based transport)
        self.client = sse_client(self.url)
        read_stream, write_stream = await self.client.__aenter__()
        
        # Initialize session
        self.session = ClientSession(read_stream, write_stream)
        await self.session.__aenter__()
        await self.session.initialize()
        
        print("✓ Connected to SSE server")
    
    async def list_tools(self):
        """List all available tools from the server."""
        if not self.session:
            raise RuntimeError("Not connected. Call connect() first.")
        
        response = await self.session.list_tools()
        
        print(f"\n📋 Available Tools ({len(response.tools)}):")
        for tool in response.tools:
            print(f"  - {tool.name}: {tool.description}")
            if tool.inputSchema and "properties" in tool.inputSchema:
                params = ", ".join(tool.inputSchema["properties"].keys())
                print(f"    Parameters: {params}")
        
        return response.tools
    
    async def call_tool(self, name: str, **kwargs):
        """Call a tool on the server."""
        if not self.session:
            raise RuntimeError("Not connected. Call connect() first.")
        
        result = await self.session.call_tool(name, kwargs)
        
        print(f"\n✓ {name}({kwargs}) →")
        for content in result.content:
            if hasattr(content, 'text'):
                print(f"  {content.text}")
        
        return result
    
    async def list_resources(self):
        """List all available resources from the server."""
        if not self.session:
            raise RuntimeError("Not connected. Call connect() first.")
        
        response = await self.session.list_resources()
        
        print(f"\n📦 Available Resources ({len(response.resources)}):")
        for resource in response.resources:
            print(f"  - {resource.uri}")
            if resource.name:
                print(f"    Name: {resource.name}")
            if resource.description:
                print(f"    Description: {resource.description}")
        
        return response.resources
    
    async def read_resource(self, uri: str):
        """Read a resource from the server."""
        if not self.session:
            raise RuntimeError("Not connected. Call connect() first.")
        
        result = await self.session.read_resource(uri)
        
        print(f"\n📄 Resource: {uri}")
        for content in result.contents:
            if hasattr(content, 'text'):
                print(f"  {content.text[:200]}...")  # Show first 200 chars
            elif hasattr(content, 'uri'):
                print(f"  URI: {content.uri}")
        
        return result
    
    async def close(self):
        """Close the connection."""
        if self.session:
            await self.session.__aexit__(None, None, None)
        if self.client:
            await self.client.__aexit__(None, None, None)
        print("✓ Connection closed")


async def main():
    """
    Test the SSE client.
    
    NOTE: This requires an MCP server running with SSE transport.
    Example server URL: http://localhost:8000/sse
    
    To run this example:
    1. Start an MCP server with SSE support on port 8000
    2. Update the URL below if using a different endpoint
    3. Run: python clients/sse_client.py
    """
    print("🚀 Testing SSE MCP Client\n")
    
    # Example server URL (update to match your server)
    server_url = "http://localhost:8000/sse"
    
    client = SSEMCPClient(server_url)
    
    try:
        # Connect to server
        await client.connect()
        
        # List available tools
        await client.list_tools()
        
        # List available resources
        await client.list_resources()
        
        # Example tool calls (adjust based on your server's tools)
        # await client.call_tool("add", a=10, b=5)
        # await client.call_tool("greet", name="SSE User")
        
        # Example resource read (adjust based on your server's resources)
        # await client.read_resource("greeting://welcome")
        
        print("\n✓ SSE client test completed!")
        print("\nℹ️  Note: Uncomment tool calls and resource reads above")
        print("   after starting an SSE-enabled MCP server")
    
    except ConnectionError as e:
        print(f"❌ Connection Error: {e}")
        print(f"\nℹ️  Make sure an MCP server is running at: {server_url}")
        print("   SSE servers expose HTTP endpoints instead of stdio")
    except Exception as e:
        print(f"❌ Error: {e}")
        import traceback
        traceback.print_exc()
    
    finally:
        await client.close()


if __name__ == "__main__":
    asyncio.run(main())
