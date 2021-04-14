FROM adoptopenjdk/openjdk11
EXPOSE 8080
ADD target/vstreem.jar vstreem.jar
ENTRYPOINT ["java","-jar","vstreem.jar"]