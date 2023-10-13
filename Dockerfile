FROM eclipse-temurin:17.0.8.1_1-jdk
VOLUME /tmp
# Set the working directory in the container to /app
WORKDIR /app
ARG JAR_FILE
# Copy the JAR_FILE into the container at /app
ADD ${JAR_FILE} /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/urandom","-jar","/app/app.jar"]