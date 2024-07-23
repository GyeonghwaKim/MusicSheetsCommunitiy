FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=build/libs/*.jar
ARG PROFILES
COPY ${JAR_FILE} app.jar
ENV TZ Asia/Seoul
ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILES}","-jar","app.jar"]
