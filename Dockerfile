# ===== Stage 1: Build =====
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy the Maven project files (pom.xml first for dependency caching)
COPY FinanceTracker/pom.xml ./pom.xml

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY FinanceTracker/src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# ===== Stage 2: Run =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the built JAR from the build stage
COPY --from=build /app/target/app.jar ./app.jar

# Switch to non-root user
USER appuser

# Run the application using the PORT environment variable provided by Render
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
