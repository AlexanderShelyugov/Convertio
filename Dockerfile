FROM maven:3-openjdk-11 AS build
WORKDIR /convertio
COPY . ./
RUN mvn dependency:resolve
RUN mvn clean package

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /convertio
COPY --from=build ./convertio/convertio-main/target/convertio.jar ./
EXPOSE 8080
ENTRYPOINT ["java","-jar","convertio.jar"]