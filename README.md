# Java-Filmorate
Бэкенд Spring Boot приложения, работащего с фильмами и оценками пользователей.

### Модель базы данных (ER-диаграмма)

![Модель базы данных](/Users/alcorw/java-filmorate/ER-диаграмма.png)

### Примеры sql-запросов к базе

1. Получение фильма с id = 7:
```sql
SELECT *
FROM films
WHERE film_id = 7;
```
2. Получение всех пользователей:
```sql
SELECT *
FROM users
```
