show databases;

-- 1ERE ÉTAPE - CRÉER LA DB
-- collate -> de quelle façon il fait les recherches (WHERE, ORDER BY, GROUP BY)
-- utf8mb4_general_ci = character insensitive (contient le "_ai")
-- utf8mb4_general = character sensitive
-- utf8mb4_general_ai = accent insensitive
CREATE DATABASE magix_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- schemas = database (synonymes)

-- rentre dans la DB afin que je puisse faire les requêtes
USE magix_db;

SHOW TABLES;

-- 2EME ÉTAPE - CRÉER L'USER
-- de n'importe où
CREATE USER magix_user IDENTIFIED BY 'AAAaaa111';
-- localement
CREATE USER magix_user@'localhost' IDENTIFIED BY 'AAAaaa111';
-- de l'adresse IP = 34.34.34.43
CREATE USER magix_user@'34.34.34.43' IDENTIFIED BY 'AAAaaa111';
-- de n'importe quel adresse IP
CREATE USER magix_user@'%' IDENTIFIED BY 'AAAaaa111';

-- supprimer user
DELETE FROM mysql.user WHERE User = 'magix_user';

-- describe la table
DESC mysql.user;

-- 3EME ÉTAPE - DONNER L'ACCÈS 
-- GRANT ALL - droit de faire tout
-- GRANT DELETE - droit de faire delete seulement
-- GRANT SELECT - droit de faire select seulement
GRANT ALL ON magix_db.* TO magix_user@'localhost';

-- tinyint (boolean), int, bigint, etc.
-- ENUM: type de données qui permet de stocker une valeur parmi un ensemble prédéfini de chaînes de caractères. La colonne ne peut contenir que l'une des valeurs définies, empêchant les erreurs liées à des entrées invalides. MySQL stocke les valeurs ENUM sous forme d'entiers en interne (ex. : pending=1, active=2, inactive=3), ce qui permet des comparaisons rapides, ce qui améliore la fiabilité des données tout en optimisant les performances.
CREATE TABLE users (
	id INT NOT NULL AUTO_INCREMENT,
	status ENUM('pending', 'active', 'inactive') DEFAULT 'pending',
    email VARCHAR(255),
    jobs JSON NOT NULL,
    PRIMARY KEY pk_users(id),
    INDEX idx_users_email(email)
) ENGINE = innoDB;

CREATE INDEX idx_users_email ON users(email);
CREATE UNIQUE INDEX uk_users_email ON users(email);

-- faire de la pagination
-- je commence à 0 et je veux combien? 10 (offset, combien)
SELECT * FROM users WHERE id > 0 LIMIT 0, 10;

INSERT INTO users(email, jobs) VALUES ('a@a.com', '["DBA", "Web"]');

-- rechercher dans un JSON, il faut utiliser JSON_CONTAINS
SELECT * FROM users WHERE JSON_CONTAINS(jobs, '"DBA"');

-- C:\Program Files\MySQL\MySQL Server 8.0\bin
-- cmd
-- mysqldump.exe -uroot -p magix_db > magix.sql
