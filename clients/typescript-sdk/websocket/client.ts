/**
 * WebSocket Transport Example Client
 *
 * Demonstrates @modelcontextprotocol/sdk's WebSocketClientTransport, which
 * connects to an MCP server over the WebSocket protocol.
 *
 * Unlike every other client example in this repo, this deliberately uses
 * the original @modelcontextprotocol/sdk (v1) package, not
 * @modelcontextprotocol/client (v2) - the WebSocket transport was dropped
 * in the v2 split (no websocket.ts under packages/client/src/client/), so
 * this transport is only reachable via v1.
 */
import { Client } from '@modelcontextprotocol/sdk/client/index.js';
import { WebSocketClientTransport } from '@modelcontextprotocol/sdk/client/websocket.js';

const client = new Client({ name: 'websocket-example-client', version: '1.0.0' });

const transport = new WebSocketClientTransport(new URL('wss://mcp.example.com/ws'));
await client.connect(transport);

const tools = await client.listTools();
console.log(`Connected via WebSocket, ${tools.tools.length} tools available`);

for (const tool of tools.tools) {
    console.log(`  - ${tool.name}`);
}

await client.close();
