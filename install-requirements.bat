@echo off
REM ============================================================
REM  RUN THIS ONCE - installs JDK 17 + Maven (needs internet).
REM  After it finishes: CLOSE this window, then use run-all.bat.
REM  You do NOT need to run this again.
REM ============================================================
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0setup.ps1"
echo.
pause
