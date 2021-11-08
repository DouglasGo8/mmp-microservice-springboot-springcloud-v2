FROM azul/zulu-openjdk-alpine:17.0.0 as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM azul/zulu-openjdk-alpine:17.0.0
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
