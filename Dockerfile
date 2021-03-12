#
# Package stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Runtime stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/src/main/resources/oas.yaml /tmp/oas/oas.yaml
COPY --from=build /home/app/target/*-fat.jar /usr/local/lib/app.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
