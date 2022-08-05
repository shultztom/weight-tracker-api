FROM openjdk:11-jdk-buster
ARG JAR_FILE=build/libs/*.jar /*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]