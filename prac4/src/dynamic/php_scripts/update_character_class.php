<?php
    $conn = new mysqli("db", "user", "password", "appDB");

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $id = $_POST['id'];
    $newCharClass = $_POST['class'];

    $sql = "UPDATE characters SET class='$newCharClass' WHERE ID='$id'";

    if ($conn->query($sql) === TRUE) {
        echo "Data updated successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }

    $conn->close();

    //header("Location: ../views/read.php");
    exit();
?>