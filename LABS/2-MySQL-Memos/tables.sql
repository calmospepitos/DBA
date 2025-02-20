-- TACHE 0
SET GLOBAL time_zone = '-5:00';

-- TACHE 1
CREATE DATABASE memos_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE memos_db;

-- TACHE 2
CREATE USER memos_user@'localhost' IDENTIFIED BY 'AAAaaa111';
GRANT ALL ON memos_db.* TO memos_user@'localhost';

-- TACHE 5
CREATE TABLE users (
	id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(40) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY pk_users(id)
) ENGINE = innoDB;

CREATE INDEX idx_users_username ON users(username);

SELECT * FROM users;

-- TACHE 7
CREATE TABLE category (
	id INT NOT NULL AUTO_INCREMENT,
    id_user INT NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY pk_category(id)
) ENGINE = innoDB;

CREATE UNIQUE INDEX uk_category_name ON category(name);
CREATE INDEX idx_category_id ON category(id);
CREATE INDEX idx_category_id_user ON category(id_user);

SELECT * FROM category;

-- TACHE 10
CREATE TABLE memos (
	id INT NOT NULL AUTO_INCREMENT,
    id_category INT NOT NULL,
    memo TEXT NOT NULL,
    created TIMESTAMP,
    PRIMARY KEY pk_memos(id)
) ENGINE = innoDB;

SELECT * FROM memos;

CREATE INDEX idx_memos_id ON memos(id);
CREATE INDEX idx_memos_created ON memos(created);

-- TACHE 11
SELECT m.*, c.id_user FROM memos m, category c WHERE m.id_category = c.id AND c.id_user = 1;