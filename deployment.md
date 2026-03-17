# Розгортання у виробничому середовищі

## Вимоги

Мінімальні вимоги до сервера: 2 ядра CPU, 2 GB RAM, 10 GB диск, ОС Windows 10 або Ubuntu 20.04. Необхідне ПЗ: Java JDK 17+, PostgreSQL 15+, Maven 3.9+, Git.

## Налаштування мережі

Відкрий порт 8080 для застосунку. Порт 5432 (PostgreSQL) має бути доступний лише локально.

Команда для відкриття порту у Windows Firewall:

    netsh advfirewall firewall add rule name="StockApp" dir=in action=allow protocol=TCP localport=8080

## Налаштування бази даних

Підключись до PostgreSQL та виконай:

    CREATE USER stockapp WITH PASSWORD 'STRONG_PASSWORD';
    CREATE DATABASE stockmanagementdatabase OWNER stockapp;
    GRANT ALL PRIVILEGES ON DATABASE stockmanagementdatabase TO stockapp;

## Конфігурація для production

Створи файл src/main/resources/application-prod.properties з таким вмістом:

    spring.datasource.url=jdbc:postgresql://localhost:5432/stockmanagementdatabase
    spring.datasource.username=stockapp
    spring.datasource.password=STRONG_PASSWORD
    spring.jpa.hibernate.ddl-auto=validate
    spring.jpa.show-sql=false
    server.port=8080
    logging.level.root=WARN
    logging.level.com.example=INFO
    logging.file.name=logs/application.log

## Розгортання

    git clone https://github.com/Vladdos02/Practica.git
    cd Practica
    mvn clean package -DskipTests
    java -jar -Dspring.profiles.active=prod target\stockmanagementbackend-0.0.1-SNAPSHOT.jar

Або через скрипт: scripts\start-prod.bat

## Запуск як служба Windows

Встанови NSSM (nssm.cc/download) та виконай:

    nssm install StockApp "java" "-jar -Dspring.profiles.active=prod C:\apps\stockapp\stockmanagementbackend-0.0.1-SNAPSHOT.jar"
    nssm set StockApp AppDirectory C:\apps\stockapp
    nssm start StockApp

## Перевірка працездатності

Виконай запит: curl http://localhost:8080/api/products

Очікується HTTP 200 з JSON-масивом. Якщо повертається 500 — перевір налаштування БД в application-prod.properties.

Перегляд логів: type logs\application.log

Шукай рядок "Started StockManagementBackendApplication" — це означає успішний запуск.
