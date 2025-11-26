FROM maven:3.9.6-eclipse-temurin-8 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -Dmaven.test.skip=true clean package

FROM tomcat:9.0.89-jdk8-temurin-jammy
ENV CATALINA_OPTS="-Djava.security.egd=file:/dev/./urandom"
COPY --from=build /workspace/target/phone_shoop-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]

