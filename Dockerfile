# Stage 1 — Build da aplicação
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2 — Runtime da aplicação
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia TUDO que o Quarkus precisa para rodar JAR legacy
COPY --from=build /app/target/quarkus-app ./quarkus-app
COPY --from=build /app/target/*-runner.jar ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
