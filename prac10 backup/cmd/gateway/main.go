package main

import "Pr10/internal/gateway"

func main() {
	go gateway.RunGrpc()
	gateway.RunGateway()
}
