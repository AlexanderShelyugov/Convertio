# Convertio

Web service that uses [exchangeratesapi.io](https://exchangeratesapi.io) to convert money amounts between currencies.

![Deploy to Heroku](https://github.com/AlexanderShelyugov/Convertio/actions/workflows/heroku.yml/badge.svg)
![Tests](https://github.com/AlexanderShelyugov/Convertio/actions/workflows/tests.yml/badge.svg)
![Health](https://img.shields.io/website?label=App%20on%20Heroku&url=https%3A%2F%2Falexander-shelyugov-convertio.herokuapp.com%2Factuator%2Fhealth)

## Table of contents
- [Usage](#Usage)
- [Build](#Build)
- [Run](#Run)

## Usage
Visit [documentation page](https://alexander-shelyugov-convertio.herokuapp.com/docs) to look at possible endpoints.

All supported currencies are listed [here](https://alexander-shelyugov-convertio.herokuapp.com/currencies).

Check ones that you need and call [/conversions/{from}/{to}/{amount}](https://alexander-shelyugov-convertio.herokuapp.com/conversions/USD/EUR/100).

You can also use available [actuators](https://alexander-shelyugov-convertio.herokuapp.com/actuator).

## Build
### Maven
```shell
mvn clean package
```
### Docker
```shell
docker build -t convertio .
```

## Run
### Execute Jar
```shell
java -jar convertio-main/target/convertio.jar
```
### Run Docker container
```shell
docker run --rm -p 8080:8080 convertio
```