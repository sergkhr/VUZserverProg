package gateway

import (
	"context"
	"fmt"
	"io"
	"log"
	"net"
	"net/http"
	"prac10/proto/generated/pb"
	"strconv"

	"github.com/grpc-ecosystem/grpc-gateway/v2/runtime"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/metadata"
)

func RunGateway() {
	var ctx = context.Background()
	var mux = runtime.NewServeMux(
		runtime.WithIncomingHeaderMatcher(HeaderMatcher),
		runtime.WithOutgoingHeaderMatcher(HeaderMatcher),
	)
	var opts = []grpc.DialOption{grpc.WithTransportCredentials(insecure.NewCredentials())}
	var err = pb.RegisterCookieServiceHandlerFromEndpoint(ctx, mux, "cookieService:12201", opts)
	err = pb.RegisterPingServiceHandlerFromEndpoint(ctx, mux, "localhost:12201", opts)
	err = pb.RegisterFileServiceHandlerFromEndpoint(ctx, mux, "filesService:12201", opts)
	mux.HandlePath("POST", "/files", handleBinaryFileUpload)
	mux.HandlePath("PUT", "/files/{id}", handleBinaryFileUpdate)
	mux.HandlePath("GET", "/files/{id}", handleFileDownload)
	if err != nil {
		panic(err)
	}
	log.Printf("server listening at 80")
	if err := http.ListenAndServe(":80", mux); err != nil {
		panic(err)
	}
}

func handleBinaryFileUpload(w http.ResponseWriter, r *http.Request, params map[string]string) {
	err := r.ParseForm()
	if err != nil {
		http.Error(w, fmt.Sprintf("failed to parse form: %s", err.Error()), http.StatusBadRequest)
		return
	}
	file, header, err := r.FormFile("file")
	if err != nil {
		http.Error(w, fmt.Sprintf("failed to get file 'attachment': %s", err.Error()), http.StatusBadRequest)
		return
	}
	conn, err := grpc.Dial("filesService:12201", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Failed to connect to server: %v", err)
	}
	defer conn.Close()
	client := pb.NewFileServiceClient(conn)
	stream, err := client.UploadFile(context.Background())
	if err != nil {
		log.Fatalf("Error opening stream: %v", err)
	}
	buf := make([]byte, 1024)
	for {
		n, err := file.Read(buf)
		if err == io.EOF {
			break
		}
		if err != nil {
			log.Fatalf("Error reading file: %v", err)
		}
		if err := stream.Send(&pb.FileTransferMessage{FileData: buf[:n], Filename: header.Filename}); err != nil {
			log.Fatalf("Error sending file chunk: %v", err)
		}
	}
	response, err := stream.CloseAndRecv()
	if err != nil {
		log.Fatalf("Error receiving response: %v", err)
	}
	fmt.Printf("Server response: %s\n", response.Message)
	w.Write([]byte("Success"))
}

func handleBinaryFileUpdate(w http.ResponseWriter, r *http.Request, params map[string]string) {
	var id = params["id"]
	err := r.ParseForm()
	if err != nil {
		http.Error(w, fmt.Sprintf("failed to parse form: %s", err.Error()), http.StatusBadRequest)
		return
	}
	file, header, err := r.FormFile("file")
	if err != nil {
		http.Error(w, fmt.Sprintf("failed to get file 'attachment': %s", err.Error()), http.StatusBadRequest)
		return
	}
	conn, err := grpc.Dial("filesService:12201", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Failed to connect to server: %v", err)
	}
	defer conn.Close()
	client := pb.NewFileServiceClient(conn)
	stream, err := client.UpdateFile(context.Background())
	if err != nil {
		log.Fatalf("Error opening stream: %v", err)
	}
	buf := make([]byte, 1024)
	for {
		n, err := file.Read(buf)
		if err == io.EOF {
			break
		}
		if err != nil {
			log.Fatalf("Error reading file: %v", err)
		}
		if err := stream.Send(&pb.FileUpdateMessage{
			FileData: buf[:n],
			Filename: header.Filename,
			FileId:   id,
		}); err != nil {
			log.Fatalf("Error sending file chunk: %v", err)
		}
	}
	response, err := stream.CloseAndRecv()
	if err != nil {
		log.Fatalf("Error receiving response: %v", err)
	}
	fmt.Printf("Server response: %s\n", response.Message)
	w.Write([]byte("Success"))
}

func handleFileDownload(res http.ResponseWriter, r *http.Request, params map[string]string) {
	var id = params["id"]
	conn, err := grpc.Dial("filesService:12201", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("Failed to connect to server: %v", err)
	}
	defer conn.Close()
	client := pb.NewFileServiceClient(conn)
	stream, err := client.DownloadFile(context.Background(), &pb.FileRequest{FileId: id})
	var filename string
	var fileData []byte
	for {
		req, err := stream.Recv()
		if err == io.EOF {
			break
		}
		if err != nil {
			fmt.Println("Error: " + err.Error())
			return
		}
		filename = req.GetFilename()
		fileData = append(fileData, req.GetFileData()...)
	}
	res.Header().Set("Content-Type", "application/octet-stream")
	res.Header().Set("Content-Disposition", fmt.Sprintf("filename=\"%s\"", filename))
	res.Write(fileData)
}

func HeaderMatcher(key string) (string, bool) {
	fmt.Println(":" + key)
	switch key {
	case "set-cookie":
		return key, true
	case "Cookie":
		return key, true
	default:
		return runtime.DefaultHeaderMatcher(key)
	}
}

func RunGrpc() {
	lis, err := net.Listen("tcp", ":12201")
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	s := grpc.NewServer()
	pb.RegisterPingServiceServer(s, &server{})
	log.Printf("server listening at %v", lis.Addr())
	if err := s.Serve(lis); err != nil {
		panic(err)
	}
}

type server struct {
	pb.UnimplementedPingServiceServer
}

func (s *server) PingHandler(ctx context.Context, in *pb.Message) (*pb.Message, error) {
	var md, ok = metadata.FromIncomingContext(ctx)
	if !ok {
		return &pb.Message{Message: "Error"}, nil
	}
	for i, v := range md {
		fmt.Println(i + " " + strconv.Itoa(len(v)))
	}
	var cookies = md.Get("cookie")
	fmt.Println(md.Len())
	fmt.Println(cookies)
	fmt.Println(len(cookies))
	//return &pb.Message{Message: in.Message}, nil
	return &pb.Message{Message: cookies[0]}, nil
}
