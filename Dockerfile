FROM maven:3.9.11-eclipse-temurin-21-alpine AS build
WORKDIR /workspace
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline
COPY src ./src
RUN mvn -B -ntp -DskipTests package
RUN cp target/*-SNAPSHOT.jar application.jar && \
    java -Djarmode=tools -jar application.jar \
    extract --layers --destination /workspace/extracted

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S gymflow && adduser -S gymflow -G gymflow
WORKDIR /application
COPY --from=build --chown=gymflow:gymflow /workspace/extracted/dependencies/ ./
COPY --from=build --chown=gymflow:gymflow /workspace/extracted/spring-boot-loader/ ./
COPY --from=build --chown=gymflow:gymflow /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build --chown=gymflow:gymflow /workspace/extracted/application/ ./
USER gymflow
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget -q -O - http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "application.jar"]
