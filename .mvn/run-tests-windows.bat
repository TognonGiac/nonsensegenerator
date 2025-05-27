@echo off
REM Imposta limite RAM e avvia i test
set MAVEN_OPTS=-Xms512m -Xmx1024m
mvn clean test
