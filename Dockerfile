FROM openjdk:11

WORKDIR /app

COPY target/pbl6-schoolsystem.jar pbl6-schoolsystem.jar

ENTRYPOINT ["java", "-jar", "pbl6-schoolsystem.jar"]

EXPOSE 5000
