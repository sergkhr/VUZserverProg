package main

import (
	"Pr10/internal/fileService"
	"os"
)

func main() {
	var config = fileService.LoadConfig()
	var application, err = fileService.NewApplication(config)
	if err != nil {
		os.Exit(1)
	}
	application.SetupRoutes()
}
