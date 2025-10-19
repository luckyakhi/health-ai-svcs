# Task API (Spring Boot)

Spring Boot 3.3 REST API implementing the provided OpenAPI for task management with JPA + H2.

## Run

Requirements: Java 17+ and Maven.

```
mvn spring-boot:run
```

Server runs on `http://localhost:4000`.

Swagger UI: `http://localhost:4000/swagger-ui.html`

## Endpoints (summary)

- `GET /tasks` — list with filters `status, group, ownerId, unassigned, q, page, pageSize`
- `POST /tasks` — create task
- `GET /tasks/{id}` — get task
- `PATCH /tasks/{id}` — partial update (send only fields to change)
- `DELETE /tasks/{id}` — delete task
- `POST /tasks/{id}/lock` — claim unassigned task, body `{ "userId": "u2" }`
- `POST /tasks/{id}/unlock` — unassign task, body optional `{ "userId": "u2" }`

## Notes

- Persistence uses H2 in-memory DB; two users are pre-seeded: `u1 (Alex)`, `u2 (Sam)`.
- `ownerId` must reference an existing user; otherwise 400 is returned.
- Pagination is 1-based per the spec.

