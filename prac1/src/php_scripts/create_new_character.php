<?php
    $conn = new mysqli("db", "user", "password", "appDB");

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $newCharName = $_POST['name'];
    $newCharClass = $_POST['class'];

    $sql = "INSERT INTO characters (name, class) VALUES ('$newCharName', '$newCharClass')";

    if ($conn->query($sql) === TRUE) {
        echo "Data inserted successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }

    $conn->close();

    //header("Location: ../views/read.php");
    exit();
?>