@echo off
echo Compiling Timetable Generator...
if not exist out mkdir out
javac -d out -sourcepath src src/Main.java
if %errorlevel% neq 0 (
    echo Compilation FAILED.
    exit /b 1
)
echo Compilation SUCCESS.
echo.
echo Running in Demo Mode (auto-selecting option 1)...
echo 1 | java -cp out Main
