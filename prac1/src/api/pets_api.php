<?php
    require_once 'api.php';

    class PetsApi extends Api{
        public $apiName = 'pets';

        /**
         * Метод GET
         * Вывод списка всех записей
         * http://ДОМЕН/api/pets.php
         * @return string
         */
        public function indexAction(){
            $mysqli = new mysqli("db", "user", "password", "appDB");
            $result = $mysqli->query("SELECT * FROM pets");
            $pets = array();
            foreach ($result as $row){
                $pets[] = $row;
            }
            $mysqli->close();
            return $this->response($pets, 200);
        }

        /**
         * Метод GET
         * Просмотр отдельной записи (по id)
         * http://ДОМЕН/api/pets.php?id=1
         * @return string
         */
        public function viewAction(){
            $id = $_GET['id'];
            if($id){
                $mysqli = new mysqli("db", "user", "password", "appDB");
                $result = $mysqli->query("SELECT * FROM pets WHERE ID='$id'");
                $pet = array();
                foreach ($result as $row){
                    $pet[] = $row;
                }
                $mysqli->close();
                return $this->response($pet, 200);
            }
            return $this->response('Data not found', 404);
        }

        /**
         * Метод POST
         * Создание новой записи
         * http://ДОМЕН/api/pets.php
         * body{
         *      species = bird,
         *      master = 1
         * }
         * @return string
         */
        public function createAction(){
            $data = json_decode(file_get_contents('php://input'), true);
            $species = $data['species'];
            $master = $data['master'];
            if($species && $master){
                $mysqli = new mysqli("db", "user", "password", "appDB");
                $sql = "INSERT INTO pets (species, master) VALUES ('$species', '$master')";
                if ($mysqli->query($sql) === TRUE) {
                    $mysqli->close();
                    return $this->response('Data created successfully', 200);
                } else {
                    $mysqli->close();
                    return $this->response('Error: ' . $sql . '<br>' . $mysqli->error, 404);
                }
            }
            return $this->response('Data not found', 404);
        }

        /**
         * Метод PUT
         * Обновление отдельной записи (по id)
         * http://ДОМЕН/api/pets.php
         * body{
         *      id = 1,
         *      master = 1
         * }
         * @return string
         */
        public function updateAction(){
            $data = json_decode(file_get_contents('php://input'), true);
            $id = $data['id'];
            $master = $data['master'];
            if($id && $master){
                $mysqli = new mysqli("db", "user", "password", "appDB");
                $sql = "UPDATE pets SET master='$master' WHERE ID='$id'";
                if ($mysqli->query($sql) === TRUE) {
                    $mysqli->close();
                    return $this->response('Data updated successfully', 200);
                } else {
                    $mysqli->close();
                    return $this->response('Error: ' . $sql . '<br>' . $mysqli->error, 404);
                }
            }
            return $this->response('Data not found', 404);
        }

        /**
         * Метод DELETE
         * Удаление отдельной записи (по id)
         * http://ДОМЕН/api/pets.php
         * body{
         *      id = 1
         * }
         * @return string
         */
        public function deleteAction(){
            $data = json_decode(file_get_contents('php://input'), true);
            $id = $data['id'];
            if($id){
                $mysqli = new mysqli("db", "user", "password", "appDB");
                $sql = "DELETE FROM pets WHERE ID='$id'";
                if ($mysqli->query($sql) === TRUE) {
                    $mysqli->close();
                    return $this->response('Data deleted successfully', 200);
                } else {
                    $mysqli->close();
                    return $this->response('Error: ' . $sql . '<br>' . $mysqli->error, 404);
                }
            }
            return $this->response('Data not found', 404);
        }
    }
?>