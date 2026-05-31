@echo off
REM ============================================================
REM  UIS Student Portal - start all 3 microservices
REM  Requires: JDK 17 and Maven on PATH (no Docker needed - H2)
REM  Just double-click this file.
REM ============================================================

echo Starting the 3 UIS microservices in separate windows...
echo.

start "exam-registration (8081)" cmd /k "cd /d %~dp0exam-registration-service && mvn spring-boot:run"
start "e-study-record (8082)"    cmd /k "cd /d %~dp0e-study-record-service && mvn spring-boot:run"
start "lectures (8083)"          cmd /k "cd /d %~dp0lectures-service && mvn spring-boot:run"

echo.
echo Three windows opened. Wait until each shows "Started ... in X seconds".
echo Then open in your browser:
echo    http://localhost:8081/   (Register for Examination)
echo    http://localhost:8082/   (E-Study Record / grades)
echo    http://localhost:8083/   (My Lectures Sheet)
echo.
echo (API test: open the UIS-bruno-collection folder in Bruno.)
pause
