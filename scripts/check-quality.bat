@echo off
cd stockManagementBackend/stockManagementBackend
echo Запуск повної перевірки...
call mvnw.cmd clean validate
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Перевірка не пройдена!
) else (
    echo [SUCCESS] Код відповідає стандартам.
)
pause