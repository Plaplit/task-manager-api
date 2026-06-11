# ── Etap 1: budowanie ──────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Kopiujemy najpierw pom.xml i pobieramy zależności (cache warstwy)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Kopiujemy kod i budujemy JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Etap 2: uruchomienie ────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Kopiujemy zbudowany JAR z etapu 1
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
