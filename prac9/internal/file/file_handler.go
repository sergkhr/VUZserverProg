package file

import (
	"bytes"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"go.mongodb.org/mongo-driver/mongo"
)

func (fs *FileService) GetFilesHandler(w http.ResponseWriter, r *http.Request) {
	// Обработка HTTP-запроса для получения списка файлов
	files, err := fs.FileRepository.GetFiles()
	if err != nil {
		log.Println(err)
		http.Error(w, "Failed to get files", http.StatusInternalServerError)
		return
	}

	// Преобразование среза файлов в формат JSON
	response, err := json.Marshal(files)
	if err != nil {
		log.Println(err)
		http.Error(w, "Failed to marshal response", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write(response)
}

func (fs *FileService) GetFileHandler(w http.ResponseWriter, r *http.Request) {
	// Обработка HTTP-запроса для получения файла по ID
	// Извлечение параметра "id" из URL
	vars := mux.Vars(r)
	fileID := vars["id"]

	// Получение файла по ID из репозитория
	fileBytes, err := fs.FileRepository.GetFileByID(fileID)
	if err != nil {
		log.Println(err)
		http.Error(w, "Failed to get file", http.StatusInternalServerError)
		return
	}

	// Отправка файла в ответе
	w.Header().Set("Content-Type", "application/octet-stream")
	w.Header().Set("Content-Disposition", "attachment; filename="+fileID)

	// Используйте ServeContent для отправки содержимого файла в ответ
	http.ServeContent(w, r, fileID, time.Now(), bytes.NewReader(fileBytes))
	w.Write(fileBytes)
}

func (fs *FileService) GetFileInfoHandler(w http.ResponseWriter, r *http.Request) {
	// Обработка HTTP-запроса для получения информации о файле по ID
	// Извлечение параметра "id" из URL
	vars := mux.Vars(r)
	fileID := vars["id"]

	// Получение информации о файле по ID из репозитория
	fileInfo, err := fs.FileRepository.GetFileInfoByID(fileID)
	if err != nil {
		log.Println(err)
		// Проверка на ошибку, если файл не найден
		if err == mongo.ErrNoDocuments {
			http.Error(w, "File not found", http.StatusNotFound)
			return
		}

		// Обработка других ошибок
		http.Error(w, "Failed to get file info", http.StatusInternalServerError)
		return
	}

	// Преобразование информации о файле в формат JSON
	response, err := json.Marshal(fileInfo)
	if err != nil {
		log.Println(err)
		http.Error(w, "Failed to marshal response", http.StatusInternalServerError)
		return
	}

	// Установка заголовков и отправка ответа
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write(response)
}

func (fs *FileService) UploadFileHandler(w http.ResponseWriter, r *http.Request) {
	// Обработка HTTP-запроса для загрузки файла
	// Извлечение файла из тела запроса
	// any file
	file, fileHeader, err := r.FormFile("file")
	if err != nil {
		log.Println(err)
		http.Error(w, "Failed to get file from request", http.StatusBadRequest)
		return
	}
	defer file.Close()

	// Загрузка файла в репозиторий
	err = fs.FileRepository.UploadFile(fileHeader)
	if err != nil {
		log.Println(err)
		http.Error(w, "Failed to upload file", http.StatusInternalServerError)
		return
	}

	// Отправка успешного ответа
	response := map[string]string{"message": "File uploaded successfully"}
	jsonResponse, err := json.Marshal(response)
	if err != nil {
		log.Println(err)
		http.Error(w, "Failed to marshal response", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write(jsonResponse)
}

func (fs *FileService) UpdateFileHandler(w http.ResponseWriter, r *http.Request) {
	// Обработка HTTP-запроса для обновления файла по ID
	// Получение id из параметра запроса
	params := mux.Vars(r)
	id := params["id"]

	// Получение файла из запроса
	_, fileHeader, err := r.FormFile("file")
	if err != nil {
		log.Println(err)
		http.Error(w, "Failed to get file from request", http.StatusBadRequest)
		return
	}

	// Вызов метода UpdateFile в FileRepository
	err = fs.FileRepository.UpdateFile(id, fileHeader)
	if err != nil {
		log.Println(err)
		http.Error(w, fmt.Sprintf("Failed to update file: %v", err), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	fmt.Fprint(w, "File updated successfully!")
}

func (fs *FileService) DeleteFileHandler(w http.ResponseWriter, r *http.Request) {
	// Обработка HTTP-запроса для удаления файла по ID
	// Получение id из параметра запроса
	params := mux.Vars(r)
	id := params["id"]

	// Вызов метода DeleteFile в FileRepository
	err := fs.FileRepository.DeleteFile(id)
	if err != nil {
		log.Println(err)
		http.Error(w, fmt.Sprintf("Failed to delete file: %v", err), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)
	fmt.Fprint(w, "File deleted successfully!")
}

func (fs *FileService) RegisterHandlers() {
	fs.Router.HandleFunc("/api/files", fs.GetFilesHandler).Methods("GET")
	fs.Router.HandleFunc("/api/files/{id}", fs.GetFileHandler).Methods("GET")
	fs.Router.HandleFunc("/api/files/{id}/info", fs.GetFileInfoHandler).Methods("GET")
	fs.Router.HandleFunc("/api/files", fs.UploadFileHandler).Methods("POST")
	fs.Router.HandleFunc("/api/files/{id}", fs.UpdateFileHandler).Methods("PUT")
	fs.Router.HandleFunc("/api/files/{id}", fs.DeleteFileHandler).Methods("DELETE")
}
