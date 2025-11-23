# Stage 1 — Build da aplicação
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2 — Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# copia o jar executável gerado
COPY --from=build /app/target/*-runner.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
