// Demonstrates the official Go MCP SDK's SSEClientTransport, which the
// sibling listfeatures/loadtest samples don't cover (those use
// StreamableClientTransport and CommandTransport, and derive their endpoint
// from a CLI flag rather than a literal). SSEClientTransport is the legacy
// SSE transport predating StreamableClientTransport.
package main

import (
	"context"
	"log"

	"github.com/modelcontextprotocol/go-sdk/mcp"
)

func main() {
	ctx := context.Background()

	transport := &mcp.SSEClientTransport{
		Endpoint: "https://mcp.example.com/sse",
	}

	client := mcp.NewClient(&mcp.Implementation{Name: "mcp-client", Version: "v1.0.0"}, nil)
	cs, err := client.Connect(ctx, transport, nil)
	if err != nil {
		log.Fatalf("failed to connect: %v", err)
	}
	defer cs.Close()
}
