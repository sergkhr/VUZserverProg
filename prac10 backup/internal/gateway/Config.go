package gateway

import "os"

type Config struct {
	fileServiceAddress   string
	cookieServiceAddress string
	port                 string
}

func LoadConfig() Config {
	var (
		fileServiceAddress   = os.Getenv("FILE_SERVICE_ADDRESS")
		cookieServiceAddress = os.Getenv("COOKIE_SERVICE_ADDRESS")
		port                 = os.Getenv("GATEWAY_PORT")
	)
	if fileServiceAddress == "" {
		fileServiceAddress = "localhost:50051"
	}
	if cookieServiceAddress == "" {
		cookieServiceAddress = "localhost:50052"
	}
	if port == "" {
		port = "80"
	}
	return Config{
		fileServiceAddress:   fileServiceAddress,
		cookieServiceAddress: cookieServiceAddress,
		port:                 port,
	}
}
