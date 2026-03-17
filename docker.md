# Запуск через Docker

## Вимоги

Встанови Docker Desktop для Windows з docker.com/products/docker-desktop.

## Запуск

Скопіюй файл змінних середовища та відредагуй пароль для БД:

    copy .env.example .env
    notepad .env

Запуск усіх сервісів:

    docker-compose up -d

API буде доступне за адресою http://localhost:8080. Перевірка статусу: docker-compose ps

## Основні команди

- docker-compose up -d — запуск у фоні
- docker-compose down — зупинка
- docker-compose logs -f app — логи застосунку
- docker-compose logs -f postgres — логи БД
- docker-compose restart app — перезапуск застосунку
- docker-compose build --no-cache — перезбірка образу

## Резервна копія БД у Docker

    docker exec stockapp-db pg_dump -U stockapp stockmanagementdatabase > backup\db_%DATE:~-4%%DATE:~3,2%%DATE:~0,2%.sql

## Відновлення БД у Docker

    docker exec -i stockapp-db psql -U stockapp -d stockmanagementdatabase < backup\db_20260316.sql
