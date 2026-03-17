# Резервне копіювання

## Стратегія

Використовуються два типи резервних копій. Щоденна — дамп бази даних, зберігається 7 днів. 
Повна — дамп БД разом з конфігурацією та JAR-файлом, створюється щонеділі та зберігається 28 днів. 
Копії перед кожним оновленням зберігаються окремо протягом 3 місяців.

## Резервна копія БД
Щоденна копія:

    pg_dump -U postgres -d stockmanagementdatabase -f backup\daily\db_%DATE:~-4%%DATE:~3,2%%DATE:~0,2%.sql
Повна копія у бінарному форматі:

    pg_dump -U postgres -d stockmanagementdatabase -F c -f backup\full\db_full_%DATE:~-4%%DATE:~3,2%%DATE:~0,2%.dump

## Резервна копія конфігурації та застосунку

    copy src\main\resources\application*.properties backup\full\
    copy target\*.jar backup\full\
    copy logs\application.log backup\full\

## Перевірка цілісності

    pg_restore --list backup\full\db_full_20260316.dump > nul && echo OK || echo CORRUPTED

## Автоматизація
Запускай скрипт scripts\backup.bat через Планувальник завдань Windows щодня о 03:00. 
Скрипт автоматично видаляє щоденні копії старші 7 днів та повні копії старші 28 днів.

## Відновлення
Повне відновлення системи:

    nssm stop StockApp
    psql -U postgres -c "DROP DATABASE IF EXISTS stockmanagementdatabase;"
    psql -U postgres -c "CREATE DATABASE stockmanagementdatabase OWNER stockapp;"
    pg_restore -U postgres -d stockmanagementdatabase backup\full\db_full_20260316.dump
    copy backup\full\application*.properties src\main\resources\
    copy backup\full\*.jar target\
    nssm start StockApp

Відновлення однієї таблиці (наприклад products):

    pg_restore -U postgres -d stockmanagementdatabase -t products backup\full\db_full_20260316.dump

Перевірка після відновлення:

    psql -U postgres -d stockmanagementdatabase -c "SELECT COUNT(*) FROM products;"
    curl http://localhost:8080/api/products
