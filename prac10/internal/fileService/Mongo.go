package fileService

import (
	"context"
	"fmt"
	"log"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

func NewDBClient(URL string) (*mongo.Client, error) {
	var clientOptions = options.Client().ApplyURI(URL)
	var client, err = mongo.Connect(context.Background(), clientOptions)
	if err != nil {
		log.Fatal(err)
		return nil, err
	}

	err = client.Ping(context.Background(), nil)
	if err != nil {
		log.Fatal(err)
		return nil, err
	}
	fmt.Println("Connected to Mongo!")
	return client, nil
}
