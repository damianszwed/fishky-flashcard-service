FROM openjdk:11-jdk
VOLUME /tmp
# Set the working directory in the container to /app
WORKDIR /app
ARG JAR_FILE
# Copy the JAR_FILE into the container at /app
ADD ${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=production", "-Djava.security.egd=file:/dev/urandom","-jar","/app.jar"]