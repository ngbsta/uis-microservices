# exam-registration-service (Kutay)

Spring Boot microservice for the **Register for Examination** module. Port **8081**.
See `../README.md` for the full 3-service setup.

## Owns
`exam_sitting`, `exam_registration` (+ `RegStatus` enum). Student/Course/Teacher live in
other services and are referenced by id.

## Layers
`controller → service (interface + Impl) → repository (JpaRepository + DatabaseConn singleton)`,
DTOs via **ModelMapper**, inter-service calls via `client/EnrollmentClient` (enrolment check)
and `client/LecturesClient` (course names) using Spring **RestTemplate**.

## Endpoints
| Method | Path | Description |
|--------|------|-------------|
| GET    | `/api/sittings`               | list exam sittings |
| GET    | `/api/sittings/{id}`          | one sitting |
| POST   | `/api/registrations`          | register (calls e-study-record to verify enrolment) |
| GET    | `/api/registrations/{id}`     | one registration |
| GET    | `/api/registrations?studentId=1` | a student's registrations |
| DELETE | `/api/registrations/{id}`     | unregister |

## Run
```bash
docker run --name dev-postgres -e POSTGRES_PASSWORD=mysecretpassword -p5432:5432 -d postgres
# start e-study-record-service (8082) first, then:
mvn spring-boot:run
curl -X POST http://localhost:8081/api/registrations -H "Content-Type: application/json" -d "{\"studentId\":1,\"sittingId\":1}"
```
