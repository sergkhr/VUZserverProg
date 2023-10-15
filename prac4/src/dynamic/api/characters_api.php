<?php
    require_once 'api.php';

    class CharactersApi extends Api{
        public $apiName = 'characters';

        /**
         * Метод GET
         * Вывод списка всех записей
         * http://ДОМЕН/api/characters.php
         * @return string
         */
        public function indexAction(){
            $mysqli = new mysqli("db", "user", "password", "appDB");
            $result = $mysqli->query("SELECT * FROM characters");
            $characters = array();
            foreach ($result as $row){
                $characters[] = $row;
            }
            $mysqli->close();
            // echo "Hello world!";
            // echo json_encode($characters);
            return $this->response($characters, 200);
            // return json_encode($characters);
            // return ("Hello world!");
        }

        /**
         * Метод GET
         * Просмотр отдельной записи (по id)
         * http://ДОМЕН/api/charachters.php?id=1
         * @return string
         */
        public function viewAction(){
            $id = $_GET['id'];
            if($id){
                $mysqli = new mysqli("db", "user", "password", "appDB");
                $result = $mysqli->query("SELECT * FROM characters WHERE ID='$id'");
                $character = array();
                foreach ($result as $row){
                    $character[] = $row;
                }
                $mysqli->close();
                return $this->response($character, 200);
                // return ("bye world!");
            }
            return $this->response('Data not found', 404);
        }

        /**
         * Метод POST
         * Создание новой записи
         * http://ДОМЕН/api/characters.php
         * body{
         *      name = John,
         *      class = warrior
         * }
         * @return string
         */
        public function createAction(){
            $data = json_decode(file_get_contents('php://input'), true);
            $name = $data['name'];
            $class = $data['class'];
            if($name && $class){
                $mysqli = new mysqli("db", "user", "password", "appDB");
                $sql = "INSERT INTO characters (name, class) VALUES ('$name', '$class')";
                if ($mysqli->query($sql) === TRUE) {
                    $mysqli->close();
                    return $this->response('Data created successfully', 200);
                } else {
                    $mysqli->close();
                    return $this->response('Error: ' . $sql . '<br>' . $mysqli->error, 500);
                }
            }
            return $this->response('Data not found', 404);
        }

        /**
         * Метод PUT
         * Обновление отдельной записи (по id)
         * http://ДОМЕН/api/characters.php
         * body{
         *      id = 1,
         *      class = warrior
         * }
         * @return string
         */
        public function updateAction(){
            $data = json_decode(file_get_contents('php://input'), true);
            $id = $data['id'];
            $class = $data['class'];
            if($id && $class){
                $mysqli = new mysqli("db", "user", "password", "appDB");
                $sql = "UPDATE characters SET class='$class' WHERE ID='$id'";
                if ($mysqli->query($sql) === TRUE) {
                    $mysqli->close();
                    return $this->response('Data updated successfully', 200);
                } else {
                    $mysqli->close();
                    return $this->response('Error: ' . $sql . '<br>' . $mysqli->error, 500);
                }
            }
            return $this->response('Data not found', 404);
        }

        /**
         * Метод DELETE
         * Удаление отдельной записи (по id)
         * http://ДОМЕН/api/characters.php
         * body{
         *      id = 1
         * }
         * @return string
         * might return error, if a pet has this character as it's master
         */
        public function deleteAction(){
            $data = json_decode(file_get_contents('php://input'), true);
            $id = $data['id'];
            if($id){
                $mysqli = new mysqli("db", "user", "password", "appDB");
                $sql = "DELETE FROM characters WHERE ID='$id'";
                if ($mysqli->query($sql) === TRUE) {
                    $mysqli->close();
                    return $this->response('Data deleted successfully', 200);
                } else {
                    $mysqli->close();
                    return $this->response('Error: ' . $sql . '<br>' . $mysqli->error, 500);
                }
            }
            return $this->response('Data not found', 404);
        }
    }
?>