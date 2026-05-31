# Mendel UIS Student Portal — Microservices

Implementation of the **Mendel University Information System (UIS)** student portal as
three standalone **Spring Boot microservices** that communicate over REST. Built from the
team's UML model (use-case, BPMN, conceptual / implementation / extended class, sequence
and activity diagrams).

Each module is owned by one team member but all three are delivered here:

| Service | Port | Module | Web UI |
|---------|------|--------|--------|
| `exam-registration-service` | 8081 | Register for Examination (Kutay Tanriverdi) | http://localhost:8081/ |
| `e-study-record-service`     | 8082 | E-Study Record / grades (Myat Noe Khin)     | http://localhost:8082/ |
| `lectures-service`           | 8083 | My Lectures Sheet (Muhammad Umer Ijaz)      | http://localhost:8083/ |

---

## 1. Tech stack
- **Java 17**, **Spring Boot 4.0.3**, **Maven**
- **Spring Data JPA** (Hibernate)
- **H2** in-memory DB (default — no install) · **PostgreSQL** (optional `postgres` profile)
- **ModelMapper** (Entity ↔ DTO) in exam-registration-service
- **Design pattern: Singleton** (`repository/DatabaseConn` in every service)
- Plain HTML/JS web UIs (served as Spring Boot static resources)

## 2. Prerequisites
- **JDK 17** (e.g. Eclipse Temurin)
- **Maven 3.9+**
- (PostgreSQL option only) **Docker**

> No Docker is needed for the default setup — all services use an in-memory H2 database.

## 3. How to run (default — H2, no Docker)

### Easiest: bat scripts
1. **First time only:** double-click **`install-requirements.bat`** → installs JDK 17 + Maven
   (needs internet). When it finishes, **close that window**.
2. Double-click **`run-all.bat`** → opens the 3 services in separate windows.
3. **`stop-all.bat`** stops them.

(If you already have JDK 17 + Maven, skip step 1.)

### Or manually — three terminals
Open one terminal per service and run:
```bash
cd exam-registration-service   &&  mvn spring-boot:run     # port 8081
cd e-study-record-service      &&  mvn spring-boot:run     # port 8082
cd lectures-service            &&  mvn spring-boot:run     # port 8083
```
Each service seeds consistent demo data on first start:
- Students: Kutay Tanriverdi (#1), Myat Noe Khin (#2), Muhammad Umer Ijaz (#3) — all enrolled in course #1 (Data Structures & Algorithms)
- One open exam sitting + course timetable, assessment sheet, materials, etc.

Then open the three UIs in a browser:
- **http://localhost:8081/** — Register for Examination
- **http://localhost:8082/** — E-Study Record (grades)
- **http://localhost:8083/** — My Lectures Sheet

> **If you ever get a 404 on `/api/...`** right after a change, stop the service and run
> `mvn clean compile` then `mvn spring-boot:run` (Spring Boot's incremental run can serve
> stale classes).

## 4. What each UI does

### 8081 — Register for Examination (covers use-case scenarios UC-01..UC-07)
- **Student Mode:** sittings grouped into *registered / can register / cannot register / past*,
  **filter** by course & type, **sort** by date/course/free seats, **register** & **log out**,
  blocked reason shown, and **vacancy monitoring** ("Watch for a place") for full sittings —
  the watcher is flagged when a seat frees up.
- **Teacher Mode:** **create / edit / delete** exam sittings with the registration window
  (registration from / until, unregister until); input is validated (capacity > 0, correct
  date order).

### 8082 — E-Study Record
- **Teacher Mode:** pick a student (dropdown by name + id), **enter / update an exam result**
  (grade A–F, credits, attempt).
- **Student Mode:** study **overview** (total credits, exams taken) and **exam history**.

### 8083 — My Lectures Sheet
- **Student Mode:** course **timetable** + attendance, **assessment sheet**, released
  **test results**, **course folder** + materials, **email notification** toggle.
- **Teacher Mode:** **maintain assessment sheet** (overall grade auto-computed from
  seminar/activity/paper), add & **release test results**, **upload documents**.

## 5. Inter-service communication
```
exam-registration-service ──GET /api/enrollments/exists?studentId&courseId──▶ e-study-record-service
```
Before a registration is accepted, exam-registration asks e-study-record whether the
student is enrolled in the sitting's course (Week-10 requirement: apps communicate).

## 6. PostgreSQL option (lecturer's setup)
```bash
docker run --name dev-postgres -e POSTGRES_PASSWORD=mysecretpassword -p5432:5432 -d postgres
# then run each service with the postgres profile:
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```
(`application-postgres.properties` in each service holds the PostgreSQL connection.)

## 7. Testing with Bruno (API client)
A ready Bruno collection is in **`UIS-bruno-collection/`** (15 requests across the three
services). Open Bruno → *Open Collection* → select that folder → Send any request.

## 8. REST endpoint reference

### exam-registration (8081)
| Method | Path |
|--------|------|
| GET | /api/sittings , /api/sittings/{id} |
| POST | /api/sittings  *(create, validated)* |
| PUT | /api/sittings/{id}  *(edit)* |
| DELETE | /api/sittings/{id}  *(delete)* |
| POST | /api/registrations  *(register; calls e-study-record)* |
| GET | /api/registrations/{id} , /api/registrations?studentId= |
| DELETE | /api/registrations/{id}  *(unregister; alerts vacancy monitors)* |
| POST | /api/monitors  *(watch a full sitting)* |
| GET | /api/monitors?studentId= |
| DELETE | /api/monitors/{id} |

### e-study-record (8082)
| Method | Path |
|--------|------|
| GET | /api/students , /api/students/{id} |
| GET | /api/students/{id}/overview , /exam-history , /credits |
| GET | /api/enrollments?studentId= , /api/enrollments/exists?studentId=&courseId= |
| POST | /api/enrollments |
| GET | /api/results?studentId= |
| POST | /api/results  *(enter grade)* |
| PUT | /api/results/{id}?grade=&attempt=&credits=  *(update grade)* |

### lectures (8083)
| Method | Path |
|--------|------|
| GET | /api/courses , /api/courses/{id} , /api/courses/{id}/lectures |
| GET | /api/courses/{id}/timetable , /folder , /materials |
| POST | /api/materials |
| GET | /api/assessment-sheets?studentId= |
| POST | /api/assessment-sheets  *(auto overall grade)* |
| PUT | /api/assessment-sheets/{id}/scores?seminar=&activity=&paper= |
| GET | /api/test-results?studentId= |
| POST | /api/test-results , PUT /api/test-results/{id}/release |
| GET | /api/notifications?studentId= , PUT /api/notifications?studentId=&courseId=&enabled= |
| GET | /api/teachers , PUT /api/courses/{id}/teacher?teacherId= |

## 9. Mapping to the UML model
- **Entities** ← conceptual / implementation class diagrams
- **Controller → Service → Repository → DTO → Entity** layering ← extended implementation class diagram
- **register() flow** (validate window / capacity / enrolment, increment count) ← sequence & activity diagrams
- **Use cases** (register, unregister, filter, vacancy monitoring, publish exam, enter grade,
  assessment sheet, timetable…) ← the per-module use-case diagrams
