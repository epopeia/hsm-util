FROM openjdk:8-jre-alpine

COPY target/hsm-util-0.0.1-SNAPSHOT.jar /opt/hsm-util.jar

WORKDIR /opt

ENTRYPOINT ["java", "-Dhsm.host=host.docker.internal", "-jar", "hsm-util.jar"]

