FROM openjdk:17-ea-17-jdk-slim
VOLUME /tmp
COPY target/gateway-service-1.0.jar GatewayService.jar
ENTRYPOINT [ "java", "-jar", "GatewayService.jar"]