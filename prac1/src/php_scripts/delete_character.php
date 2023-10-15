<?php
    $conn = new mysqli("db", "user", "password", "appDB");

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $id = $_POST['id'];

    $sql = "DELETE FROM characters WHERE ID='$id'";
    if ($conn->query($sql) === TRUE) {
        echo "Data deleted successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }

    $conn->close();

    //header("Location: ../views/read.php");
    exit();
?>