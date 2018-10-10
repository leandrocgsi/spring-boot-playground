# Spring Boot Playground

[![Build Status](https://travis-ci.org/leandrocgsi/SpringBootPlayground.svg?branch=master)](https://travis-ci.org/leandrocgsi/SpringBootPlayground)
[![Code Coverage](https://codecov.io/github/leandrocgsi/SpringBootPlayground/coverage.svg)](https://codecov.io/gh/leandrocgsi/SpringBootPlayground)


# How to restore database

You just need create an empty database in MySQL with name "_spring_boot_playground_", check if your credentials are "root" and "root" and execute following command in pom.xml folder.

```sh
mvn flyway:migrate
```
