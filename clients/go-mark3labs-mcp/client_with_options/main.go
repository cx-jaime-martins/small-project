// Demonstrates the mark3labs/mcp-go client package's option-taking/legacy
// SSE constructors (NewStdioMCPClientWithOptions, NewSSEMCPClient), which
// the sibling filesystem_stdio_client/roots_http_client samples don't cover
// (those use NewStdioMCPClient and NewStreamableHttpClient). Unlike the
// low-level client/transport constructors, both of these also build and
// start the client itself in one call.
package main

import (
	"fmt"

	"github.com/mark3labs/mcp-go/client"
)

func main() {
	stdioClient, err := client.NewStdioMCPClientWithOptions(
		"uvx",
		nil,
		[]string{"mcp-server-git==1.4.0", "--repository", "/repo"},
	)
	if err != nil {
		panic(err)
	}
	defer stdioClient.Close()

	sseClient, err := client.NewSSEMCPClient("https://mcp.example.com/sse")
	if err != nil {
		panic(err)
	}
	defer sseClient.Close()

	fmt.Println("connected")
}
