FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /convertio
COPY . ./
RUN mvn dependency:resolve
RUN mvn clean install

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /convertio
COPY --from=build ./convertio/convertio-main/target/convertio.jar ./
EXPOSE 8080
ENTRYPOINT ["java","-jar","convertio.jar"]