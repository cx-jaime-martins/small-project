/**
 * SSE Transport Example Client (@modelcontextprotocol/client v2)
 *
 * The TS SDK's client half was split into its own package at 2.0.0. All
 * other client-v2 usage in this repo (typescript-sdk/sse-polling) derives
 * its URL from a CLI arg, so it never resolves to a literal MCP server
 * address; this sample uses a fixed remote endpoint instead, to exercise
 * @modelcontextprotocol/client's SSEClientTransport with a resolvable value.
 */
import { Client } from '@modelcontextprotocol/client';
import { SSEClientTransport } from '@modelcontextprotocol/client/sse';

const client = new Client({ name: 'sse-v2-example-client', version: '1.0.0' });

const transport = new SSEClientTransport(new URL('https://mcp.example.com/sse'));
await client.connect(transport);

const tools = await client.listTools();
console.log(`Connected via SSE, ${tools.tools.length} tools available`);

for (const tool of tools.tools) {
    console.log(`  - ${tool.name}`);
}

await client.close();
