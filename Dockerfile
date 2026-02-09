# stage 1 - build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# RUN mvn -DskipTests package
RUN mvn clean verify

# stage 2 - runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# pega qualquer jar gerado (evitando .jar.original)
#COPY --from=build /app/target/*.jar /app/app.jar
COPY --from=build /app/target/desafio-votacao-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]