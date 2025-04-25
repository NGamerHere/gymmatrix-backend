FROM eclipse-temurin:21-jdk-jammy as deps

WORKDIR /app

COPY . /app

RUN chmod +x gradlew

RUN ./gradlew build --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=deps /app/build/libs/gymmatrix-0.0.1-SNAPSHOT.jar /app/gymmatrix.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/gymmatrix.jar"]
