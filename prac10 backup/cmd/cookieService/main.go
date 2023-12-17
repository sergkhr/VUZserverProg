package main

import "Pr10/internal/cookieService"

func main() {
	var config = cookieService.LoadConfig()
	var application = cookieService.NewApplication(config)
	application.SetupHandlers()
}
