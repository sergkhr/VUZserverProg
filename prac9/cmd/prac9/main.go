package main

import (
	"log"
	"os"

	//"prac9/internal"
	"prac9/internal/file"
)

func main() {
	initEnv()

	port := os.Getenv("PORT")
	log.Println("PORT is set to", port)
	if port == "" {
		log.Fatal("PORT is not set in the environment")
	}

	mongoURI := os.Getenv("MONGO_URI")
	if mongoURI == "" {
		log.Fatal("MONGO_URI is not set in the environment")
	}

	fileRepository, err := file.NewFileRepository(mongoURI)
	if err != nil {
		log.Fatalf("Failed to initialize file repository: %v", err)
	}

	fileService := file.NewFileService(fileRepository)

	log.Printf("Server is listening on :%s...\n", port)
	fileService.Run(":" + port)

	// server := internal.NewServer()
	// server.Run(":" + port)
}

func initEnv() {
	// .env file is not seen by Go - stupid Go
	os.Setenv("PORT", init_port)
	os.Setenv("COOKIE_NAME", init_cookie_name)
	os.Setenv("MONGO_URI", init_mongo_uri)
}
