# Stage 1 — Build da aplicação
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2 — Runtime da aplicação
FROM eclipse-temurin:21-jre
WORKDIR /app

# copia o conteúdo gerado em target/quarkus-app
COPY --from=build /app/target/quarkus-app ./

EXPOSE 8080

CMD ["java", "-jar", "quarkus-run.jar"]
