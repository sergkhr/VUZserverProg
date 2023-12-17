package cookieService

import "os"

type Config struct {
	port       string
	cookieName string
}

func LoadConfig() Config {
	var (
		port       = os.Getenv("PORT")
		cookieName = os.Getenv("COOKIE_NAME")
	)
	if port == "" {
		port = "80"
	}
	if cookieName == "" {
		cookieName = "cookie"
	}
	return Config{
		port:       port,
		cookieName: cookieName,
	}
}
