# Stage 1 — Build da aplicação
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY . .

# Corrige o erro: dar permissão de execução ao mvnw
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

# Stage 2 — Runtime da aplicação
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*-SNAPSHOT.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
