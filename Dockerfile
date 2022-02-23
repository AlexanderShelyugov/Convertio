FROM maven:3-openjdk-11-slim AS build
WORKDIR /convertio
COPY . ./
RUN mvn clean package

FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /convertio
COPY --from=build ./convertio/convertio-main/target/convertio.jar ./
EXPOSE 8080
ENTRYPOINT ["java","-jar","convertio.jar"]