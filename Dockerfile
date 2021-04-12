FROM openjdk:latest
EXPOSE 8080 5432
ADD target/user-registration.jar user-registration.jar
ENTRYPOINT ["java","-jar","user-registration.jar"]