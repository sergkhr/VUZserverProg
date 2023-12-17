package fileService

import (
	"context"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net"
	"net/http"
	"prac10/proto/generated/pb"

	"github.com/gorilla/mux"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"google.golang.org/grpc"
	"google.golang.org/protobuf/types/known/emptypb"
)

type Application struct {
	config      Config
	fileService *FileService
}

func NewApplication(config Config) (*Application, error) {
	var mongo, err = NewDBClient(config.databaseURL)
	if err != nil {
		return nil, err
	}

	var fileService *FileService
	fileService, err = NewFileService(mongo)

	return &Application{
		config:      config,
		fileService: fileService,
	}, nil
}

type server struct {
	pb.UnimplementedFileServiceServer
	fileService FileService
}

func (this *Application) SetupRoutes() {
	lis, err := net.Listen("tcp", ":12201")
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	s := grpc.NewServer()
	pb.RegisterFileServiceServer(s, &server{
		UnimplementedFileServiceServer: pb.UnimplementedFileServiceServer{},
		fileService:                    *this.fileService,
	})
	log.Printf("server listening at %v", lis.Addr())
	if err := s.Serve(lis); err != nil {
		panic(err)
	}
}

func (this *server) UploadFile(stream pb.FileService_UploadFileServer) error {
	var fileData []byte
	var filename string
	for {
		req, err := stream.Recv()
		if err == io.EOF {
			break
		}
		if err != nil {
			fmt.Println("Error: " + err.Error())
			return err
		}
		filename = req.GetFilename()
		fileData = append(fileData, req.GetFileData()...)
	}
	fmt.Println(filename)
	fmt.Println(len(fileData))
	this.fileService.saveFile(fileData, filename)
	return stream.SendAndClose(&pb.FileUploadResult{Message: "Success"})
}

func (this *server) UpdateFile(stream pb.FileService_UpdateFileServer) error {
	var fileData []byte
	var filename string
	var fileId string
	for {
		req, err := stream.Recv()
		if err == io.EOF {
			break
		}
		if err != nil {
			fmt.Println("Error: " + err.Error())
			return err
		}
		filename = req.GetFilename()
		fileId = req.FileId
		fileData = append(fileData, req.GetFileData()...)
	}
	var id, err = primitive.ObjectIDFromHex(fileId)
	if err != nil {
		return err
	}
	this.fileService.deleteFile(id)
	this.fileService.updateFile(fileData, filename, id)
	return stream.SendAndClose(&pb.FileUploadResult{Message: "Success"})
}

func (this *server) FilesInfoGetHandler(ctx context.Context, _ *emptypb.Empty) (*pb.AllFileInfoResponse, error) {
	fmt.Println("Files list get")
	var files, err = this.fileService.getFilesList()
	if err != nil {
		return nil, err
	}
	var infos []*pb.FileInfo
	for _, v := range files {
		infos = append(infos, &pb.FileInfo{
			Id:   v.ID.Hex(),
			Name: v.Name,
			Size: v.Size,
		})
	}
	return &pb.AllFileInfoResponse{Files: infos}, nil
}

func (this *server) DownloadFile(req *pb.FileRequest, stream pb.FileService_DownloadFileServer) error {
	fmt.Println("File download request: " + req.FileId)
	var id, err = primitive.ObjectIDFromHex(req.FileId)
	if err != nil {
		return err
	}
	var file []byte
	file, err = this.fileService.loadFile(id)
	var info *FileInfo
	info, err = this.fileService.getFileInformation(id)
	if err != nil {
		return err
	}
	err = stream.Send(&pb.FileTransferMessage{
		Filename: info.Name,
		FileData: file,
	})
	if err != nil {
		return err
	}
	return nil
}

func (this *server) FileInfoGetHandler(ctx context.Context, req *pb.FileRequest) (*pb.FileInfo, error) {
	fmt.Println("File info get")
	var id, err = primitive.ObjectIDFromHex(req.FileId)
	if err != nil {
		return nil, err
	}
	var info *FileInfo
	info, err = this.fileService.getFileInformation(id)
	if err != nil {
		return nil, err
	} else {
		return &pb.FileInfo{
			Id:   info.ID.Hex(),
			Name: info.Name,
			Size: info.Size,
		}, nil
	}
}

func (this *server) DeleteFileHandler(ctx context.Context, req *pb.FileRequest) (*emptypb.Empty, error) {
	fmt.Println("File info get")
	var id, err = primitive.ObjectIDFromHex(req.FileId)
	if err != nil {
		return nil, err
	}
	this.fileService.deleteFile(id)
	return &emptypb.Empty{}, err
}

func (this *Application) getFile(res http.ResponseWriter, req *http.Request) {
	fmt.Println("File get")
	var id, err = primitive.ObjectIDFromHex(mux.Vars(req)["id"])
	if err != nil {
		respondWithError(res, http.StatusBadRequest, "Error parsing id")
		return
	}
	var file []byte
	file, err = this.fileService.loadFile(id)
	if err != nil {
		respondWithError(res, http.StatusBadRequest, "Error loading file: "+err.Error())
		return
	}
	var fileInfo *FileInfo
	fileInfo, err = this.fileService.getFileInformation(id)
	if err != nil {
		respondWithError(res, http.StatusBadRequest, "")
	}
	res.Header().Set("Content-Type", "application/octet-stream")
	res.Header().Set("Content-Disposition", fmt.Sprintf("filename=\"%s\"", fileInfo.Name))
	res.Write(file)
}

func (this *Application) updateFile(res http.ResponseWriter, req *http.Request) {
	var _, _, err = req.FormFile("file")
	if err != nil {
		respondWithError(res, http.StatusBadRequest, "Cant get file header")
		return
	}
	var id primitive.ObjectID
	id, err = primitive.ObjectIDFromHex(mux.Vars(req)["id"])
	if err != nil {
		respondWithError(res, http.StatusBadRequest, "Error parsing id")
		return
	}
	this.fileService.deleteFile(id)
}

func (this *Application) deleteFile(res http.ResponseWriter, req *http.Request) {
	var id, err = primitive.ObjectIDFromHex(mux.Vars(req)["id"])
	if err != nil {
		respondWithError(res, http.StatusBadRequest, "Error parsing id")
		return
	}
	this.fileService.deleteFile(id)
}

func respondWithError(w http.ResponseWriter, code int, message string) {
	respondWithJSON(w, code, map[string]string{"error": message})
}

func respondWithJSON(res http.ResponseWriter, code int, payload interface{}) {
	res.Header().Set("Content-Type", "application/json")
	res.WriteHeader(code)
	json.NewEncoder(res).Encode(payload)
}
