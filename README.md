# Spring Boot + PostgreSQL starter

A notes app in Spring Boot 4 and Java 21 that keeps its data in PostgreSQL, ready to deploy on
[OceanLaunch](https://oceanlaunch.cloud). It has a page at `/` and a JSON API:

| Endpoint | What it does |
|---|---|
| `GET /api/notes` | The newest 100 notes |
| `POST /api/notes` | Adds one: `{"text": "Buy milk"}` |
| `DELETE /api/notes/{id}` | Deletes one |
| `GET /actuator/health` | Health check, including the database |

Tables are made by [Flyway](https://documentation.red-gate.com/flyway) from `src/main/resources/db/migration`.
To change them, add a new file such as `V2__add_title.sql`; it runs on the next deploy.

## Deploy it

In the OceanLaunch portal, open **Templates** and choose **Spring Boot + PostgreSQL**. The portal creates a
managed database (or uses one you pick), connects it to the app, and deploys. Your app is live at its
`*.apps.oceanlaunch.cloud` address in a few minutes.

## Make it yours

1. On GitHub, choose **Use this template** to get your own copy of this repository.
2. In the portal, open your app, and under **Make it yours** give your new repository's address.
3. Change the code and push: every push to `main` deploys a new version.

## Run it on your computer

Start a PostgreSQL with the settings in `application.properties`, for example with Docker:

```bash
docker run -d --name notes-db -p 5432:5432 -e POSTGRES_USER=app -e POSTGRES_PASSWORD=app -e POSTGRES_DB=app postgres:17-alpine
mvn spring-boot:run
```

Then open http://localhost:8080. `mvn test` runs the tests against a PostgreSQL in Docker.
