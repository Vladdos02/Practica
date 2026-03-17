# Інформаційна система управління запасами

Веб-додаток для дистанційного обліку та резервування товарів на складі агропідприємства.

## Архітектура

Система побудована за трирівневою архітектурою. 
Клієнт — браузер (HTML / CSS / JavaScript) — взаємодіє з сервером через REST API. 
Application Server реалізовано на Java 17 + Spring Boot 3 з вбудованим Tomcat, він обробляє 
бізнес-логіку та запити. 
База даних — PostgreSQL 15 — зберігає дані про товари, користувачів та резервації.
Веб-сервер окремо не виноситься, Spring Boot використовує вбудований Tomcat. 
Сервіси кешування, файлове сховище та черги повідомлень у поточній версії не використовуються.

## Встановлення залежностей

Необхідне ПЗ: Java JDK 17, Maven 3.9+, PostgreSQL 15, Git.

**Java 17.** Завантаж Eclipse Temurin JDK 17 з adoptium.net, 
під час встановлення постав галочки "Add to PATH" та "Set JAVA_HOME". 
Перевірка: java -version

**Maven.** Завантаж Apache Maven з maven.apache.org, розпакуй у 
C:\Program Files\Apache\maven, додай %MAVEN_HOME%\bin до змінної PATH. 
Перевірка: mvn -version

**PostgreSQL.** Завантаж PostgreSQL 15 з postgresql.org/download/windows, 
запам'ятай пароль для користувача postgres, порт залиш 5432.

**Git.** Завантаж Git for Windows з git-scm.com/download/win.

## Клонування репозиторію

    git clone https://github.com/Vladdos02/Practica.git
    cd Practica

## Налаштування бази даних

    psql -U postgres -c "CREATE DATABASE stockmanagementdatabase;"

## Налаштування конфігурації

Відкрий src/main/resources/application.properties і встанови свій пароль:

    spring.datasource.url=jdbc:postgresql://localhost:5432/stockmanagementdatabase
    spring.datasource.username=postgres
    spring.datasource.password=YOUR_PASSWORD
    spring.jpa.hibernate.ddl-auto=update

## Запуск у режимі розробки

    mvn spring-boot:run

Або через скрипт: scripts\start-dev.bat
API буде доступне за адресою http://localhost:8080. Перевірка: curl http://localhost:8080/api/products

## Основні команди

- mvn spring-boot:run — запуск у режимі розробки
- mvn clean package -DskipTests — збірка JAR
- mvn test — запуск тестів
- scripts\start-dev.bat — запуск через скрипт (dev)
- scripts\start-prod.bat — запуск через скрипт (prod)
- scripts\build.bat — збірка через скрипт

## Структура проєкту

Папка src/main/java містить: controller (REST контролери), service (бізнес-логіка), repository (робота з БД), model (сутності), dto (об'єкти передачі даних), exception (обробка помилок). Папка docs містить документацію, scripts — скрипти автоматизації.
