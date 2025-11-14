# # JDK 17 for Spring Boot
# FROM eclipse-temurin:17-jdk-alpine

# WORKDIR /app

# # Install netcat ( wait-for-it) == for good db connection handling
# RUN apk update && apk add --no-cache bash netcat-openbsd


# COPY mvnw pom.xml ./
# COPY .mvn .mvn

# # Copy wait-for-it script and make executable
# COPY wait-for-it.sh ./
# RUN chmod +x wait-for-it.sh

# # Install dependencies
# RUN ./mvnw dependency:go-offline -B

# # Copy all backend code
# COPY . .

# # Package Spring Boot app
# RUN ./mvnw clean package -DskipTests

# # Expose backend port
# EXPOSE 8080

# # Start Spring Boot app with wait-for-it - using 'db' instead of 'mysql'
# CMD ["./wait-for-it.sh", "db:3306", "--", "sh", "-c", "java -jar target/*.jar"]








# FROM eclipse-temurin:17-jdk-alpine

# WORKDIR /app

# RUN apk update && apk add --no-cache bash netcat-openbsd

# # Copy initial files and make mvnw executable for dependency download
# COPY mvnw pom.xml ./
# COPY .mvn .mvn
# RUN chmod +x mvnw

# COPY wait-for-it.sh ./
# RUN chmod +x wait-for-it.sh

# # Download dependencies offline
# RUN ./mvnw dependency:go-offline -B

# # Copy all backend code (this may overwrite mvnw)
# COPY . .

# # Fix permissions again for mvnw
# RUN chmod +x mvnw

# # Build the app
# RUN ./mvnw clean package -DskipTests

# EXPOSE 8080

# CMD ["./wait-for-it.sh", "db:3306", "--", "sh", "-c", "java -jar target/*.jar"]










# Use JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Install bash, netcat, and CA certificates for HTTPS
RUN apk update && apk add --no-cache bash netcat-openbsd ca-certificates

# Copy Maven wrapper and pom.xml first
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Make Maven wrapper executable
RUN chmod +x mvnw

# Copy wait-for-it.sh and make it executable
COPY wait-for-it.sh ./
RUN chmod +x wait-for-it.sh

# Optional: Copy pre-downloaded Maven cache if available
# COPY .m2 /root/.m2

# Download dependencies offline
RUN ./mvnw dependency:go-offline -B

# Copy all backend source code
COPY . .

# Fix permissions again after full copy
RUN chmod +x mvnw wait-for-it.sh

# Build the Spring Boot application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests -B -Dmaven.wagon.http.retryHandler.count=5

# Expose backend port
EXPOSE 8080

# Start the application once DB is ready
CMD ["./wait-for-it.sh", "db:3306", "--", "sh", "-c", "java -jar target/*.jar"]
