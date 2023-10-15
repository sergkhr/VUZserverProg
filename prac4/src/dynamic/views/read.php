<html lang="en">
<meta charset="UTF-8">
<head>
<title>READ part</title>
    <link rel="stylesheet" href="../style.css" type="text/css"/>
</head>
<body>

    <a href="create.php">create</a>
    <a href="update.php">update</a>
    <a href="delete.php">delete</a>
    <a href="/">static</a>

    <h1>READ часть задания</h1>
    <h1>Таблица персонажей</h1>
    <table>
        <tr><th>Id</th><th>Name</th><th>Class</th></tr>
    <?php
        $mysqli = new mysqli("db", "user", "password", "appDB");
        $result = $mysqli->query("SELECT * FROM characters");
        foreach ($result as $row){
            echo "<tr><td>{$row['ID']}</td><td>{$row['name']}</td><td>{$row['class']}</td></tr>";
        }
    ?>
    </table>

    <h1>Таблица питомцев</h1>
    <table>
        <tr><th>Id</th><th>species</th><th>master</th></tr>
    <?php
        $result = $mysqli->query("SELECT * FROM pets");
        foreach ($result as $row){
            echo "<tr><td>{$row['ID']}</td><td>{$row['species']}</td><td>{$row['master']}</td></tr>";
        }

        $mysqli->close();
    ?>
    </table>

    
    <?php
    // phpinfo();
    ?>
</body>
</html>