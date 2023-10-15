CREATE DATABASE IF NOT EXISTS appDB;
CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'password';
GRANT SELECT,UPDATE,INSERT,DELETE ON appDB.* TO 'user'@'%';
FLUSH PRIVILEGES;

USE appDB;
CREATE TABLE IF NOT EXISTS characters (
    ID INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    class VARCHAR(40) NOT NULL,
    PRIMARY KEY (ID)
);

INSERT INTO characters (name, class)
SELECT * FROM (SELECT 'Alex', 'Warrior') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM characters WHERE name = 'Alex' AND class = 'Warrior'
) LIMIT 1;

INSERT INTO characters (name, class)
SELECT * FROM (SELECT 'Fred', 'Mage') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM characters WHERE name = 'Fred' AND class = 'Mage'
) LIMIT 1;

INSERT INTO characters (name, class)
SELECT * FROM (SELECT 'Emily', 'Priest') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM characters WHERE name = 'Emily' AND class = 'Priest'
) LIMIT 1;


CREATE TABLE IF NOT EXISTS pets (
    ID INT(11) NOT NULL AUTO_INCREMENT,
    species VARCHAR(20) NOT NULL,
    master INT(11) NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (master) REFERENCES characters(ID)
);

INSERT INTO pets (species, master)
SELECT * FROM (SELECT 'Dog', 1) AS tmp
WHERE NOT EXISTS (
    SELECT species FROM pets WHERE species = 'Dog' AND master = 1
) LIMIT 1;

INSERT INTO pets (species, master)
SELECT * FROM (SELECT 'Cat', 2) AS tmp
WHERE NOT EXISTS (
    SELECT species FROM pets WHERE species = 'Cat' AND master = 2
) LIMIT 1;

INSERT INTO pets (species, master)
SELECT * FROM (SELECT 'Bird', 3) AS tmp
WHERE NOT EXISTS (
    SELECT species FROM pets WHERE species = 'Bird' AND master = 1
) LIMIT 1;
