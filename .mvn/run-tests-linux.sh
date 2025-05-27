#!/bin/bash
# Imposta limite RAM e avvia i test
export MAVEN_OPTS="-Xms512m -Xmx1024m"
mvn clean test
