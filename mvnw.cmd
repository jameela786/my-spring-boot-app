@echo off
setlocal

set DIRNAME=%~dp0
set APP_BASE_NAME=%~n0%
set APP_HOME=%DIRNAME%

if exist "%APP_HOME%\src\main\java\com\example\myapp\MySpringBootAppApplication.java" (
  set SCRIPT="%APP_HOME%\src\main\java\com\example\myapp\MySpringBootAppApplication.java"
) else (
  echo Cannot find main class: com.example.myapp.MySpringBootAppApplication
  exit /b 1
)

set CLASSPATH=%APP_HOME%\target\classes;%APP_HOME%\target\dependency\*

"%JAVA_HOME%\bin\java" -cp "%CLASSPATH%" %JAVA_OPTS% %SCRIPT% %*