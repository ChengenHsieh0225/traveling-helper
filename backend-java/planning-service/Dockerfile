FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY auth-service/pom.xml ./auth-service/
COPY planning-service/pom.xml ./planning-service/
COPY social-service/pom.xml ./social-service/

RUN mvn dependency:go-offline -B

COPY . .
RUN mvn clean package -DskipTests -pl planning-service -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/planning-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]