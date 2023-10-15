<html lang="en">
<meta charset="UTF-8">
<head>
<title>DELETE part</title>
    <link rel="stylesheet" href="../style.css" type="text/css"/>
</head>
<body>

    <a href="read.php">read</a>
    <a href="create.php">create</a>
    <a href="update.php">update</a>

    <h1>DELETE часть задания</h1>
    <h1>введите ID кого хотите удалить<h1>
    <form method="POST" action="../php_scripts/delete_character.php">
        <input type="number" name="id" placeholder="id"/>
        <input type="submit" name="deleteChar" value="delete_character"/>
    </form>
</body>
</html>