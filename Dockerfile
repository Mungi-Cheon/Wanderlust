#FROM openjdk:17
#
## /home/ubuntu/app
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#
#ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","app.jar"]

FROM openjdk:17
RUN mkdir -p /home/ubuntu/app
WORKDIR /home/ubuntu/app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /home/ubuntu/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/home/ubuntu/app/app.jar"]

