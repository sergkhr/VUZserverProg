package api

import (
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"sync"
	"time"

	"github.com/gorilla/securecookie"
)

var cookieHandler = securecookie.New(
	securecookie.GenerateRandomKey(64),
	securecookie.GenerateRandomKey(32),
)

func GetDataHandler(w http.ResponseWriter, r *http.Request) {
	// Simulate some processing time with sleep
	time.Sleep(2 * time.Second)
	time.Sleep(2 * time.Second)

	// Retrieve data from cookie
	var cookie_name = os.Getenv("COOKIE_NAME")
	data, err := r.Cookie(cookie_name)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	value := make(map[string]interface{})
	err = cookieHandler.Decode(cookie_name, data.Value, &value)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// Return the data
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(value)
}

func StoreDataHandler(w http.ResponseWriter, r *http.Request) {
	// Simulate some processing time with sleep
	time.Sleep(2 * time.Second)
	time.Sleep(2 * time.Second)

	var cookie_name = os.Getenv("COOKIE_NAME")

	// Decode JSON request
	var requestData map[string]interface{}
	err := json.NewDecoder(r.Body).Decode(&requestData)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Encode data into cookie
	encoded, err := cookieHandler.Encode(cookie_name, requestData)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	cookie := &http.Cookie{
		Name:     cookie_name,
		Value:    encoded,
		Path:     "/",
		HttpOnly: true,
		Secure:   false, // Change to true in production with HTTPS
	}

	http.SetCookie(w, cookie)

	w.WriteHeader(http.StatusOK)
	fmt.Fprint(w, "Data stored successfully!")
}

func ConcurrentGetDataHandler(w http.ResponseWriter, r *http.Request) {
	// Simulate some processing time with sleep
	var wg sync.WaitGroup
	wg.Add(2)
	go func() {
		time.Sleep(2 * time.Second)
		wg.Done()
	}()
	go func() {
		time.Sleep(2 * time.Second)
		wg.Done()
	}()

	// Retrieve data from cookie
	var cookie_name = os.Getenv("COOKIE_NAME")
	data, err := r.Cookie(cookie_name)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	value := make(map[string]interface{})
	err = cookieHandler.Decode(cookie_name, data.Value, &value)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	// Return the data
	wg.Wait()
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(value)
}

func ConcurrentStoreDataHandler(w http.ResponseWriter, r *http.Request) {
	// Simulate some processing time with sleep
	var wg sync.WaitGroup
	wg.Add(2)
	go func() {
		time.Sleep(2 * time.Second)
		wg.Done()
	}()
	go func() {
		time.Sleep(2 * time.Second)
		wg.Done()
	}()

	var cookie_name = os.Getenv("COOKIE_NAME")

	// Decode JSON request
	var requestData map[string]interface{}
	err := json.NewDecoder(r.Body).Decode(&requestData)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Encode data into cookie
	encoded, err := cookieHandler.Encode(cookie_name, requestData)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	cookie := &http.Cookie{
		Name:     cookie_name,
		Value:    encoded,
		Path:     "/",
		HttpOnly: true,
		Secure:   false, // Change to true in production with HTTPS
	}

	http.SetCookie(w, cookie)

	wg.Wait()
	w.WriteHeader(http.StatusOK)
	fmt.Fprint(w, "Data stored successfully!")
}
