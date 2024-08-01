#!/bin/bash

ROOT_PATH="/home/ubuntu/backend"
VERSION=$(grep -E '^version=' "$ROOT_PATH/gradle.properties" | cut -d'=' -f2)
JAR="$ROOT_PATH/application.jar"

APP_LOG="$ROOT_PATH/application.log"
ERROR_LOG="$ROOT_PATH/error.log"
START_LOG="$ROOT_PATH/start.log"

NOW=$(date +%c)

echo "[$NOW] $JAR 복사" >> $START_LOG
cp $ROOT_PATH/build/libs/backend-$VERSION.jar" "$JAR

echo "[$NOW] > $JAR 실행" >> $START_LOG
nohup java -Dspring.profiles.active=prod -jar $JAR > $APP_LOG 2> $ERROR_LOG &

SERVICE_PID=$(pgrep -f $JAR)
echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG
