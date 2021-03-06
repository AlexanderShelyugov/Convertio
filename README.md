# Convertio

Web service that uses [Currency Converter](https://rapidapi.com/natkapral/api/currency-converter5/) from [rapidapi.com](https://rapidapi.com) to convert money amounts between currencies.

Note, that this project is fully tested! 😉

![Deploy to Heroku](https://github.com/AlexanderShelyugov/Convertio/actions/workflows/heroku.yml/badge.svg)
![Tests](https://github.com/AlexanderShelyugov/Convertio/actions/workflows/tests.yml/badge.svg)
![Health-Heroku](https://img.shields.io/website?label=App%20on%20Heroku&url=https%3A%2F%2Falexander-shelyugov-convertio.herokuapp.com%2Factuator%2Fhealth)

[//]: # (![Health-AWS]&#40;https://img.shields.io/website?label=App%20on%20AWS&url=http%3A%2F%2Fconvertio-env.eba-6e8nxdry.us-east-1.elasticbeanstalk.com%2Factuator%2Fhealth&#41;)

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