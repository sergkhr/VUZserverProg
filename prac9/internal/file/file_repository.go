package file

import (
	"context"
	"io/ioutil"
	"log"
	"mime/multipart"
	"time"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/gridfs"
	"go.mongodb.org/mongo-driver/mongo/options"
)

const (
	dbName         = "filesdb"
	collectionName = "files"
)

type FileRepository struct {
	client   *mongo.Client
	database *mongo.Database
	gridFS   *gridfs.Bucket
}

type File struct {
	ID         primitive.ObjectID `bson:"_id,omitempty"`
	FileName   string             `bson:"filename"`
	FileSize   int64              `bson:"filesize"`
	UploadTime time.Time          `bson:"uploadtime"`
}

func NewFileRepository(mongoURI string) (*FileRepository, error) {
	client, err := mongo.Connect(context.Background(),
		options.Client().ApplyURI(mongoURI).
			SetAuth(options.Credential{
				Username: "admin",
				Password: "admin",
			}))
	if err != nil {
		return nil, err
	}

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	err = client.Ping(ctx, nil)
	if err != nil {
		return nil, err
	}

	db := client.Database(dbName)
	gridFS, err := gridfs.NewBucket(db)
	if err != nil {
		return nil, err
	}

	return &FileRepository{
		client:   client,
		database: db,
		gridFS:   gridFS,
	}, nil
}

func (fr *FileRepository) GetFiles() ([]*File, error) {
	// Реализация получения списка файлов из коллекции
	log.Println("Getting files")

	var files []*File
	// cur, err := fr.database.Collection(collectionName).Find(context.Background(), bson.D{})
	cur, err := fr.gridFS.GetFilesCollection().Find(context.Background(), bson.D{})
	log.Println("error collection")
	log.Println(err)
	if err != nil {
		return nil, err
	}
	defer cur.Close(context.Background())

	for cur.Next(context.Background()) {
		var file File
		err := cur.Decode(&file)
		log.Println("error decode")
		log.Println(err)
		if err != nil {
			log.Fatal(err)
		}
		files = append(files, &file)
	}

	return files, nil
}

func (fr *FileRepository) GetFileByID(id string) ([]byte, error) {
	// Реализация получения файла по ID из GridFS
	// Преобразование строки в ObjectId
	objectID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return nil, err
	}

	// Реализация получения файла по ID из GridFS
	file, err := fr.gridFS.OpenDownloadStream(objectID)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	// Чтение содержимого файла в массив байтов
	fileBytes, err := ioutil.ReadAll(file)
	if err != nil {
		return nil, err
	}

	return fileBytes, nil
}

func (fr *FileRepository) GetFileInfoByID(id string) (*File, error) {
	// Реализация получения информации о файле по ID из коллекции
	// Преобразование строки в ObjectId
	objectID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return nil, err
	}

	// Реализация получения информации о файле по ID из коллекции
	var file File
	err = fr.gridFS.GetFilesCollection().FindOne(context.Background(), bson.D{{Key: "_id", Value: objectID}}).Decode(&file)
	if err != nil {
		return nil, err
	}

	return &file, nil
}

func (fr *FileRepository) UploadFile(fileHeader *multipart.FileHeader) error {
	//check if we are connected to the database
	log.Println("uploading file")
	err := fr.client.Ping(context.Background(), nil)
	if err != nil {
		return err
	}

	// Реализация загрузки файла в GridFS
	filename := fileHeader.Filename
	file, err := fileHeader.Open()

	log.Println(fileHeader.Filename)
	log.Println(fileHeader.Size)
	log.Println(file)

	if err != nil {
		return err
	}
	defer file.Close()

	// uploadStream, err := fr.gridFS.OpenUploadStream(fileHeader.Filename)
	// log.Println("error OpenUploadStream")
	// log.Println(err)
	// if err != nil {
	// 	return err
	// }
	// defer uploadStream.Close()

	var _, err_2 = fr.gridFS.UploadFromStream(filename, file)
	log.Println("error UploadFromStream")
	log.Println(err_2)
	if err_2 != nil {
		return err_2
	}
	// log.Println("File upload")
	// log.Println(fr)
	// log.Println(fr.gridFS)

	return nil

	// _, err = io.Copy(uploadStream, file)
	// if err != nil {
	// 	return err
	// }

	// return nil
}

func (fr *FileRepository) UpdateFile(id string, fileHeader *multipart.FileHeader) error {
	// Реализация обновления файла в GridFS
	// (возможно, нужно сначала удалить старый файл)

	err := fr.DeleteFile(id)
	if err != nil {
		return err
	}

	return fr.UploadFile(fileHeader)
}

func (fr *FileRepository) DeleteFile(id string) error {
	// Реализация удаления файла из GridFS
	objectID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return err
	}
	return fr.gridFS.Delete(objectID)
}
