package main

import (
	"log"
	"os"
	"prac8/internal"
)

func main() {
	initEnv()

	port := os.Getenv("PORT")
	log.Println("PORT is set to", port)
	if port == "" {
		log.Fatal("PORT is not set in the environment")
	}

	server := internal.NewServer()
	server.Run(":" + port)
}

func initEnv() {
	// .env file is not seen by Go - stupid Go
	os.Setenv("PORT", init_port)
	os.Setenv("COOKIE_NAME", init_cookie_name)
}
