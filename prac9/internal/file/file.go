package file

import (
	"net/http"

	"github.com/gorilla/mux"
)

type FileService struct {
	Router         *mux.Router
	FileRepository *FileRepository
}

func NewFileService(fileRepository *FileRepository) *FileService {
	fileService := &FileService{
		Router:         mux.NewRouter(),
		FileRepository: fileRepository,
	}

	fileService.RegisterHandlers()

	return fileService
}

func (f *FileService) Run(addr string) {
	http.Handle("/", f.Router)
	http.ListenAndServe(addr, nil)
}
