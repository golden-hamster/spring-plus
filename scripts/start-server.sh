#!/bin/bash

echo "----------서버 시작------------"
docker stop spring-plus || true
docker rm spring-plus || true
docker pull 767397671057.dkr.ecr.ap-northeast-2.amazonaws.com/spring-plus:latest
docker run -d --name spring-plus -p 80:8080 767397671057.dkr.ecr.ap-northeast-2.amazonaws.com/spring-plus:latest
echo "----------서버 배포 끝--------"