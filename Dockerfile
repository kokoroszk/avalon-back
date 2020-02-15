FROM openjdk:latest

RUN mkdir /app
WORKDIR /app
COPY ./gradlew ./build.gradle ./settings.gradle /app/
COPY ./src /app/src
COPY ./gradle /app/gradle
ENTRYPOINT ["sh", "./gradlew", "bootRun"]
