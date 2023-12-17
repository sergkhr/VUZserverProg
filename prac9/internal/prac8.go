package internal

import (
	"github.com/gorilla/mux"
	"github.com/gorilla/securecookie"
)

type MyGoService struct {
	Router       *mux.Router
	CookieSecret []byte
}

func NewMyGoService() *MyGoService {
	return &MyGoService{
		Router:       mux.NewRouter(),
		CookieSecret: securecookie.GenerateRandomKey(64),
	}
}

func (s *MyGoService) Run(addr string) {
	RunServer(s.Router, addr)
}
