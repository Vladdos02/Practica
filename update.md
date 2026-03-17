# Оновлення системи

## Підготовка

Перевір поточні версії Java та PostgreSQL — вони мають відповідати вимогам нової версії. 
Переглянь зміни між версіями командою:

    git log --oneline HEAD..origin/main

Заплануй час простою близько 5-10 хвилин, бажано в неробочий час. 
Повідом користувачів заздалегідь.

## Резервна копія перед оновленням

    pg_dump -U postgres -d stockmanagementdatabase -f backup\pre_update_%DATE:~-4%%DATE:~3,2%%DATE:~0,2%.sql
    mkdir backup\app_%DATE:~-4%%DATE:~3,2%%DATE:~0,2%
    copy target\*.jar backup\app_%DATE:~-4%%DATE:~3,2%%DATE:~0,2%\
    copy src\main\resources\application*.properties backup\app_%DATE:~-4%%DATE:~3,2%%DATE:~0,2%\

## Процес оновлення

Зупини застосунок: nssm stop StockApp

Отримай новий код: git pull origin main

Перевір зміни в конфігурації командою git diff HEAD~1 HEAD -- 
src/main/resources/application.properties. Якщо з'явились нові параметри — 
додай їх у application-prod.properties.

Збери та запусти:

    mvn clean package -DskipTests
    nssm start StockApp

## Перевірка після оновлення

Виконай curl http://localhost:8080/api/products і перевір логи на помилки:

    type logs\application.log | findstr /i "error warn"

## Процедура відкату (Rollback)

Якщо після оновлення щось пішло не так, зупини застосунок командою nssm stop StockApp.

Відновлення попередньої версії (вкажи дату своєї резервної копії, наприклад 20260316):

    copy backup\app_20260316\*.jar target\
    copy backup\app_20260316\application*.properties src\main\resources\

Якщо змінилась схема БД — відновлення з дампу:

    psql -U postgres -c "DROP DATABASE stockmanagementdatabase;"
    psql -U postgres -c "CREATE DATABASE stockmanagementdatabase OWNER stockapp;"
    psql -U postgres -d stockmanagementdatabase -f backup\pre_update_20260316.sql

Запусти попередню версію: nssm start StockApp

Зафіксуй відкат у Git:

    git revert HEAD
    git push origin main
