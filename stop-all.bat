@echo off
REM Stops all running services (kills Java processes).
echo Stopping all Java (Spring Boot) processes...
taskkill /F /IM java.exe /T 2>nul
echo Done.
pause
