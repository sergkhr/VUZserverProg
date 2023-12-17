package fileService

import (
	"fmt"
	"os"
)

type Config struct {
	databaseURL string
	port        string
}

func LoadConfig() Config {
	var (
		databaseURL = os.Getenv("DATABASE_URL")
		port        = os.Getenv("APP_PORT")
	)
	if databaseURL == "" {
		databaseURL = "localhost"
	}
	if port == "" {
		port = "80"
	}
	fmt.Println("Port: " + port)
	fmt.Println("Database URL: " + "mongodb://" + databaseURL)
	return Config{
		databaseURL: "mongodb://" + databaseURL,
		port:        port,
	}
}
