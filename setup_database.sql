-- Script de configuration de la base de données pour phone_shoop
-- Exécutez ce script avec MySQL pour créer la base de données

-- Créer la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS phone_shoop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de données
USE phone_shoop;

-- Les tables seront créées automatiquement par Hibernate grâce à hbm2ddl.auto=update
-- Mais voici la structure attendue pour référence :

-- Table Category
-- CREATE TABLE IF NOT EXISTS Category (
--     categoryId INT PRIMARY KEY AUTO_INCREMENT,
--     categoryTitle VARCHAR(255),
--     categoryDescription TEXT
-- );

-- Table Product
-- CREATE TABLE IF NOT EXISTS Product (
--     pId INT PRIMARY KEY AUTO_INCREMENT,
--     pName VARCHAR(255),
--     pDesc TEXT,
--     pPrice DECIMAL(10,2),
--     pDiscount INT,
--     pQuantity INT,
--     pPhoto VARCHAR(255),
--     category_categoryId INT,
--     FOREIGN KEY (category_categoryId) REFERENCES Category(categoryId)
-- );

-- Table User
-- CREATE TABLE IF NOT EXISTS User (
--     userId INT PRIMARY KEY AUTO_INCREMENT,
--     userName VARCHAR(255),
--     userEmail VARCHAR(255),
--     userPassword VARCHAR(255),
--     userPhone VARCHAR(20),
--     userPic VARCHAR(255),
--     userAddress TEXT,
--     userType VARCHAR(20) DEFAULT 'normal'
-- );

-- Afficher un message de confirmation
SELECT 'Base de données phone_shoop créée avec succès!' AS Message;

