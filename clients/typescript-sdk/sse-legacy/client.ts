/**
 * SSE (Legacy) Transport Example Client
 *
 * Demonstrates @modelcontextprotocol/sdk's SSEClientTransport with a literal
 * endpoint. The sibling sse-polling sample (under typescript-client-v2)
 * exercises the same class via the v2 package with a CLI-derived URL; this
 * one uses the v1 package with a fixed remote endpoint to keep the example
 * self-contained and resolvable.
 */
import { Client } from '@modelcontextprotocol/sdk/client/index.js';
import { SSEClientTransport } from '@modelcontextprotocol/sdk/client/sse.js';

const client = new Client({ name: 'sse-legacy-example-client', version: '1.0.0' });

const transport = new SSEClientTransport(new URL('https://mcp.example.com/sse'));
await client.connect(transport);

const tools = await client.listTools();
console.log(`Connected via SSE, ${tools.tools.length} tools available`);

for (const tool of tools.tools) {
    console.log(`  - ${tool.name}`);
}

await client.close();
