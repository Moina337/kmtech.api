# --- Étape 1 : build ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copié séparément pour profiter du cache Docker : si pom.xml ne change pas,
# les dépendances ne sont pas retéléchargées à chaque build.
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Étape 2 : exécution ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Ne pas exécuter l'application en root à l'intérieur du conteneur
RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /app/uploads && chown -R spring:spring /app

USER spring
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
