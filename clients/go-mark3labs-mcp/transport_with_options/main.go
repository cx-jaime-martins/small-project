// Demonstrates the mark3labs/mcp-go client/transport package's
// option-taking constructor variants (NewCommandWithEnv,
// NewCommandWithOptions, NewStdioWithOptions, NewSSE), which the sibling
// transport_package_client sample doesn't cover (that one uses NewCommand
// and NewStreamableHTTP only). Real code reaches for these when it needs
// an explicit env slice, transport-level options, or the legacy SSE
// transport specifically.
package main

import (
	"context"
	"log"

	"github.com/mark3labs/mcp-go/client"
	"github.com/mark3labs/mcp-go/client/transport"
)

func main() {
	// NewCommandWithEnv: same shape as NewCommand, plus an explicit env slice.
	cmdTransport := transport.NewCommandWithEnv("uvx", []string{"PATH=/usr/bin"}, "mcp-server-git==1.4.0", "--repository", "/repo")

	cmdClient := client.NewClient(cmdTransport)
	if err := cmdClient.Start(context.Background()); err != nil {
		log.Fatalf("failed to start command client: %v", err)
	}
	defer cmdClient.Close()

	// NewCommandWithOptions: like NewCommandWithEnv, but args is an
	// explicit slice rather than variadic, so transport-level options can
	// follow it.
	optsTransport := transport.NewCommandWithOptions("npx", nil, []string{"-y", "@modelcontextprotocol/server-filesystem@0.6.2"})

	optsClient := client.NewClient(optsTransport)
	if err := optsClient.Start(context.Background()); err != nil {
		log.Fatalf("failed to start options client: %v", err)
	}
	defer optsClient.Close()

	// NewStdioWithOptions: the explicit-args-slice sibling of NewStdio.
	stdioTransport := transport.NewStdioWithOptions("npx", nil, []string{"-y", "@modelcontextprotocol/server-everything"})

	stdioClient := client.NewClient(stdioTransport)
	if err := stdioClient.Start(context.Background()); err != nil {
		log.Fatalf("failed to start stdio client: %v", err)
	}
	defer stdioClient.Close()

	// NewSSE: the legacy SSE transport, constructed directly rather than
	// through client.NewSSEMCPClient.
	sseTransport, err := transport.NewSSE("https://mcp.example.com/sse")
	if err != nil {
		log.Fatalf("failed to create SSE transport: %v", err)
	}

	sseClient := client.NewClient(sseTransport)
	if err := sseClient.Start(context.Background()); err != nil {
		log.Fatalf("failed to start SSE client: %v", err)
	}
	defer sseClient.Close()
}
