# Stage 1: Build the backend server
FROM gradle:8-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Cấp quyền thực thi cho gradlew
RUN chmod +x gradlew

# Build the fat jar for the server module
RUN ./gradlew :server:buildFatJar --no-daemon

# Stage 2: Create the minimal runtime image
FROM eclipse-temurin:21-jre
EXPOSE 8085
RUN mkdir /app

# Copy FAT Jar từ bước 1
COPY --from=build /home/gradle/src/server/build/libs/*-all.jar /app/ktor-server.jar

# Run Application
ENTRYPOINT ["java","-jar","/app/ktor-server.jar"]
