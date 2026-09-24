# mcp-config-fixtures

Fixture repository with MCP client config files for testing the MCP config analyzer.

## Covered clients

| File | Client | Format |
|------|--------|--------|
| `claude_desktop_config.json` | Claude Desktop | `mcpServers` JSON |
| `.cursor/mcp.json` | Cursor | `mcpServers` JSON |
| `.vscode/settings.json` | VS Code (settings) | nested `mcp.mcpServers` JSON |
| `.vscode/mcp.json` | VS Code (dedicated) | `servers` JSON |
| `.zed/settings.json` | Zed | `context_servers` JSON |
| `.gemini/settings.json` | Gemini CLI | `mcpServers` JSON |
| `opencode.json` | OpenCode | flat `mcp` map with command slices |
| `.codex/config.toml` | OpenAI Codex | TOML `[mcp_servers]` |
| `mcp.json` | Windsurf / generic | `mcpServers` JSON |

## Server types covered

- `npx` → npm packages
- `uvx` → PyPI packages
- `docker run` → Docker images
- `bun x` → npm (via bun)
- `url` → remote SSE servers
