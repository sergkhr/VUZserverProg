<html lang="en">
<meta charset="UTF-8">
<head>
<title>CREATE part</title>
    <link rel="stylesheet" href="../style.css" type="text/css"/>
</head>
<body>

    <a href="read.php">read</a>
    <a href="update.php">update</a>
    <a href="delete.php">delete</a>

    <h1>CREATE часть задания</h1>
    <h1>введите имя и класс персонажа для добавления</h1>
    <form method="POST" action="../php_scripts/create_new_character.php">
        <input type="text" name="name" placeholder="name"/>
        <input type="text" name="class" placeholder="class"/>
        <input type="submit" name="createChar" value="create_new_character"/>
    </form>
</body>
</html>