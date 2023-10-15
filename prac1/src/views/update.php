<html lang="en">
<meta charset="UTF-8">
<head>
<title>UPDATE part</title>
    <link rel="stylesheet" href="../style.css" type="text/css"/>
</head>
<body>

    <a href="read.php">read</a>
    <a href="create.php">create</a>
    <a href="delete.php">delete</a>

    <h1>UPDATE часть задания</h1>
    <h1>выберите кого хотите обновить, затем введите новое значение класса</h1>
    <form method="POST" action="../php_scripts/update_character_class.php">
        <input type="number" name="id" placeholder="id"/>
        <input type="text" name="class" placeholder="class"/>
        <input type="submit" name="updateChar" value="update_character_class"/>
    </form>
</body>
</html>