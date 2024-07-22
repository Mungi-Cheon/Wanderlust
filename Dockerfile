FROM openjdk:17

# /home/ubuntu/app
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-Dspring.profiles.active=prod","-jar","app.jar"]