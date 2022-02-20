# Convertio

Web service that uses [exchangeratesapi.io](https://exchangeratesapi.io) to convert money amounts between currencies.

## Usage
Visit [documentation page](https://alexander-shelyugov-convertio.herokuapp.com/docs) to look at possible endpoints.

You can also use available [actuators](https://alexander-shelyugov-convertio.herokuapp.com/actuator).

## Health
![Deploy to Heroku](https://github.com/AlexanderShelyugov/Convertio/actions/workflows/heroku.yml/badge.svg)
![Tests](https://github.com/AlexanderShelyugov/Convertio/actions/workflows/tests.yml/badge.svg)

## Build

### Maven

Just build it as a maven project

```shell
mvn clean install
```

### Docker
```shell
docker build -t convertio .
```