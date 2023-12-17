package main

import "prac10/internal/gateway"

func main() {
	go gateway.RunGrpc()
	gateway.RunGateway()
}
