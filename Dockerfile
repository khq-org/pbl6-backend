FROM openjdk:11

WORKDIR /app

ARG SOURCE_PATH=target/pbl6-schoolsystem.jar

ARG DESTINATION_PATH=pbl6-schoolsystem.jar

COPY ${SOURCE_PATH} ${DESTINATION_PATH}

ENTRYPOINT ["java", "-jar", "pbl6-schoolsystem.jar"]

EXPOSE 5000