FROM openjdk:11

WORKDIR /app

ARG SOURCE_PATH=/target/pbl6-schoolsystem-backend.jar

ARG DESTINATION_PATH=schoolsystem.jar

COPY ${SOURCE_PATH} ${DESTINATION_PATH}

ENTRYPOINT ["java", "-jar", "schoolsystem.jar"]

EXPOSE 5000