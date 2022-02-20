FROM alpine/git AS repository
WORKDIR /convertio
RUN git clone https://github.com/AlexanderShelyugov/JavaCurrencyConvertor.git

FROM maven:jdk11 AS build
WORKDIR /convertio
COPY --from=repository /convertio/pom.xml ./pom.xml
COPY --from=repository /convertio/convertio-* ./
RUN mvn install

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /convertio
COPY --from=build ./convertio/convertio-main/target/convertio.jar ./
EXPOSE 8001
ENTRYPOINT ["java","-jar","convertio.jar"]