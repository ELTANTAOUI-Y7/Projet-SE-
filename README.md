# E-commerce Website Project

## Overview
This project is an e-commerce website built using Java EE (JEE) technologies, along with HTML, CSS, and Bootstrap for the frontend. Hibernate is used for object-relational mapping.

## Technologies Used
- Java EE (JEE)
- HTML
- CSS
- Bootstrap
- Hibernate


## Features
  - Product catalog
  - Shopping cart functionality
  - Order management

## Setup
1. Clone the repository
2. Install Java 11 and Maven
3. Configure your MySQL instance and update the credentials in `src/main/resources/hibernate.cfg.xml`
4. Ensure the `phone_shoop` schema exists (Hibernate will create the tables)

## Run locally
1. From the project root run `mvn jetty:run`
2. Open `http://localhost:8080/` to access the application
3. Stop Jetty with `Ctrl + C` (or `taskkill /PID <PID> /F` if needed)