FROM openjdk:17

# /home/ubuntu/app
WORKDIR /app
COPY *.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","app.jar"]