package internal

import (
	"net/http"
	"prac8/api"

	"github.com/gorilla/mux"
)

func NewServer() *MyGoService {
	myService := NewMyGoService()

	// /api/data is for the direct
	myService.Router.HandleFunc("/api/data", api.GetDataHandler).Methods("GET")
	myService.Router.HandleFunc("/api/data", api.StoreDataHandler).Methods("POST")

	// /data is for the reverse proxy
	// myService.Router.HandleFunc("/data", api.GetDataHandler).Methods("GET")
	// myService.Router.HandleFunc("/data", api.StoreDataHandler).Methods("POST")

	// /data/concurrent is for the concurrent reverse proxy
	myService.Router.HandleFunc("/api/data/concurrent", api.ConcurrentGetDataHandler).Methods("GET")
	myService.Router.HandleFunc("/api/data/concurrent", api.ConcurrentStoreDataHandler).Methods("POST")

	return myService
}

func RunServer(router *mux.Router, addr string) {
	http.Handle("/", router)
	err := http.ListenAndServe(addr, nil)
	if err != nil {
		panic(err)
	}
}
