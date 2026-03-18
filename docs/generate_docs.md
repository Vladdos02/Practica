# Інструкція з генерації документації

## Опис
Цей документ містить інструкції щодо генерації автоматичної документації з коду проєкту за допомогою Javadoc.

## Вимоги
- JDK 17 або вище
- Maven 3.x

## Генерація документації через Maven

1. Відкрийте термінал/командний рядок
2. Перейдіть до кореневої директорії проєкту
3. Виконайте команду:
```bash
mvn javadoc:javadoc
```
4. Після успішної генерації документація буде доступна за шляхом:
   `target/site/apidocs/index.html`

## Генерація документації через командний рядок (альтернативний спосіб)
```bash
javadoc -d docs/javadoc -sourcepath src/main/java -subpackages com.example.stockmanagementbackend -encoding UTF-8 -charset UTF-8
```

## Перегляд документації

Відкрийте файл `target/site/apidocs/index.html` у веб-браузері.

## Структура документації

Документація включає опис наступних пакетів:
- `com.example.stockmanagementbackend.model` - моделі даних (entities)
- `com.example.stockmanagementbackend.repository` - репозиторії для роботи з БД
- `com.example.stockmanagementbackend.service` - бізнес-логіка
- `com.example.stockmanagementbackend.controller` - REST контролери
- `com.example.stockmanagementbackend.dto` - об'єкти передачі даних
- `com.example.stockmanagementbackend.exception` - кастомні винятки
