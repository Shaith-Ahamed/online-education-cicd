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








FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

RUN apk update && apk add --no-cache bash netcat-openbsd

# Copy initial files and make mvnw executable for dependency download
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw

COPY wait-for-it.sh ./
RUN chmod +x wait-for-it.sh

# Download dependencies offline
RUN ./mvnw dependency:go-offline -B

# Copy all backend code (this may overwrite mvnw)
COPY . .

# Fix permissions again for mvnw
RUN chmod +x mvnw

# Build the app
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["./wait-for-it.sh", "db:3306", "--", "sh", "-c", "java -jar target/*.jar"]
