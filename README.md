## Запуск через Docker

1. Нужно скомпилировать код в .jar файл.

```bash
./gradlew clean build
```
2. Запуск контейнеров (может потребоваться два запуска, т.к при первом запуске проходят миграции в бд)
```bash
docker compose up
```
3. Swagger UI доступен по эндпойнту
```bash
http://localhost:8080/swagger-ui/index.html
```

