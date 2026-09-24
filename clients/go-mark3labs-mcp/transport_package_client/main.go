// Demonstrates constructing a mark3labs/mcp-go transport directly via the
// low-level client/transport package, rather than through the client
// package's higher-level NewStdioMCPClient/NewStreamableHttpClient
// wrappers (see the sibling filesystem_stdio_client/roots_http_client
// samples). Real code reaches for this when it needs transport-level
// options client.NewStdioMCPClient/NewStreamableHttpClient don't expose
// directly.
package main

import (
	"context"
	"log"

	"github.com/mark3labs/mcp-go/client"
	"github.com/mark3labs/mcp-go/client/transport"
)

func main() {
	// Low-level stdio transport: same shape as client.NewStdioMCPClient,
	// but constructed as its own step so transport-level options
	// (e.g. WithCommandLogger) can be applied before the client wraps it.
	stdioTransport := transport.NewCommand("uvx", "mcp-server-git==1.4.0", "--repository", "/repo")

	stdioClient := client.NewClient(stdioTransport)
	if err := stdioClient.Start(context.Background()); err != nil {
		log.Fatalf("failed to start stdio client: %v", err)
	}
	defer stdioClient.Close()

	// Low-level Streamable HTTP transport: same shape as
	// client.NewStreamableHttpClient, again constructed separately from
	// the client that wraps it.
	httpTransport, err := transport.NewStreamableHTTP("https://mcp.example.com/mcp")
	if err != nil {
		log.Fatalf("failed to create HTTP transport: %v", err)
	}

	httpClient := client.NewClient(httpTransport)
	if err := httpClient.Start(context.Background()); err != nil {
		log.Fatalf("failed to start HTTP client: %v", err)
	}
	defer httpClient.Close()
}
