package cookieService

import (
	"Pr10/proto/generated/pb"
	"context"
	"encoding/json"
	"fmt"
	"google.golang.org/grpc"
	"google.golang.org/grpc/metadata"
	"google.golang.org/protobuf/types/known/emptypb"
	"log"
	"net"
	"net/http"
	"strconv"
	"strings"
	"sync"
	"time"
)

type Application struct {
	cookieWorker CookieWorker
}

func NewApplication(config Config) *Application {
	return &Application{
		cookieWorker: NewCookieWorker(config),
	}
}

type server struct {
	pb.UnimplementedCookieServiceServer
	cookieWorker CookieWorker
}

func (this *Application) SetupHandlers() {
	lis, err := net.Listen("tcp", ":12201")
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	s := grpc.NewServer()
	pb.RegisterCookieServiceServer(s, &server{
		UnimplementedCookieServiceServer: pb.UnimplementedCookieServiceServer{},
		cookieWorker:                     this.cookieWorker,
	})
	log.Printf("server listening at %v", lis.Addr())
	if err := s.Serve(lis); err != nil {
		panic(err)
	}
}

func (this *server) CookieCreate(ctx context.Context, req *pb.StringDataMessage) (*pb.StringDataMessage, error) {
	var data = CookieData{
		Data: req.Data,
	}
	var cookie, _ = this.cookieWorker.createCookie(data)
	var md = metadata.Pairs("Set-Cookie", cookie.Name+"="+cookie.Value)
	return req, grpc.SetHeader(ctx, md)
}

func (this *server) LinearCookieGet(ctx context.Context, req *emptypb.Empty) (*pb.StringDataMessage, error) {
	var md, _ = metadata.FromIncomingContext(ctx)
	for i, v := range md {
		fmt.Println(i + " " + strconv.Itoa(len(v)))
	}
	var cookies = md.Get("cookie")
	if len(cookies) == 0 {
		return &pb.StringDataMessage{Data: "Error getting cookie"}, nil
	}
	var keyValue = strings.Split(cookies[0], "=")
	var cookieData, err = this.cookieWorker.getData(keyValue[0], keyValue[1])
	if err != nil {
		return &pb.StringDataMessage{Data: "Error parsing cookie"}, nil
	}
	time.Sleep(time.Second)
	time.Sleep(time.Second)
	return &pb.StringDataMessage{Data: cookieData.Data}, nil
}

func (this *server) ParallelCookieGet(ctx context.Context, req *emptypb.Empty) (*pb.StringDataMessage, error) {
	var md, _ = metadata.FromIncomingContext(ctx)
	for i, v := range md {
		fmt.Println(i + " " + strconv.Itoa(len(v)))
	}
	var cookies = md.Get("cookie")
	if len(cookies) == 0 {
		return &pb.StringDataMessage{Data: "Error getting cookie"}, nil
	}
	var keyValue = strings.Split(cookies[0], "=")
	var cookieData, err = this.cookieWorker.getData(keyValue[0], keyValue[1])
	if err != nil {
		return &pb.StringDataMessage{Data: "Error parsing cookie"}, nil
	}

	var wg = sync.WaitGroup{}
	wg.Add(2)
	go func() {
		time.Sleep(time.Second)
		wg.Done()
	}()
	go func() {
		time.Sleep(time.Second)
		wg.Done()
	}()
	wg.Wait()
	return &pb.StringDataMessage{Data: cookieData.Data}, nil
}

func respondWithError(w http.ResponseWriter, code int, message string) {
	respondWithJSON(w, code, map[string]string{"error": message})
}

func respondWithJSON(res http.ResponseWriter, code int, payload interface{}) {
	res.Header().Set("Content-Type", "application/json")
	res.WriteHeader(code)
	json.NewEncoder(res).Encode(payload)
}
