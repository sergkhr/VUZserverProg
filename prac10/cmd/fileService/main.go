package main

import (
	"os"
	"prac10/internal/fileService"
)

func main() {
	var config = fileService.LoadConfig()
	var application, err = fileService.NewApplication(config)
	if err != nil {
		os.Exit(1)
	}
	application.SetupRoutes()
}
