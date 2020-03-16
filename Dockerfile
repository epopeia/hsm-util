FROM openjdk:8-alpine

COPY target/hsm-util-0.0.1-SNAPSHOT.jar /opt/hsm-util.jar

WORKDIR /opt

ENTRYPOINT ["java", "-jar", "hsm-util.jar"]

