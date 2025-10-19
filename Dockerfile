# Simple runtime image expecting a pre-built Spring Boot JAR in target/
# Build JAR locally first: mvn -DskipTests package

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the fat JAR built by Spring Boot (deterministic name)
ARG JAR_FILE=target/app.jar
COPY ${JAR_FILE} /app/app.jar

# Expose the application port
EXPOSE 4000

# Allow passing extra JVM args via JAVA_TOOL_OPTIONS
ENV JAVA_TOOL_OPTIONS=""

# Run the Spring Boot application
ENTRYPOINT ["java","-jar","/app/app.jar"]
