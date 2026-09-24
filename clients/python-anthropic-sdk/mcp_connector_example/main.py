"""Demonstrates the Anthropic Messages API's MCP connector: referencing one
or more external MCP servers directly via the `mcp_servers` request
parameter, with no separate MCP client SDK involved.

https://docs.anthropic.com/en/docs/agents-and-tools/mcp-connector
"""

import anthropic

client = anthropic.Anthropic()


def main() -> None:
    response = client.beta.messages.create(
        model="claude-sonnet-4-5",
        max_tokens=1024,
        messages=[{"role": "user", "content": "What's my Stripe account balance?"}],
        mcp_servers=[
            {
                "type": "url",
                "url": "https://mcp.stripe.com",
                "name": "stripe",
            },
        ],
        extra_headers={"anthropic-beta": "mcp-client-2025-04-04"},
    )

    for block in response.content:
        print(block)


if __name__ == "__main__":
    main()
