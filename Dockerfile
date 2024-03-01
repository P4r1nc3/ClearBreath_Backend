FROM maven:3-openjdk-17 AS build
COPY backend/ .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/clearbreath-0.0.1-SNAPSHOT.jar clearbreath.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "clearbreath.jar"]