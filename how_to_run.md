# How to run and config the application

This application is written with Java 11 and is compatible with java 17.
You can give a profile as a parameter. Without a profile, it will use by default.

### Initial Configuration
1. Apache Maven (http://maven.apache.org) This code has been compiled with Java version 11.
2. Git Client (http://git-scm.com)
3. Clone repository from [here](https://github.com/ma-sharifi/recipeapi)

### Run
```shell
mvn spring-boot:run
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
OR
You can build application and package it into jar file. 
```shell
mvn package
```
You can run the tests with:
```shell
mvn test
```
You can execute the jar:
```shell
java -jar target/recipea.jar
```

Also, you can change below properties and `application.yaml` in defualt profile and `application-prod.yaml` in production file.
this is the key for JWT we used. for production you need use a strong key
>jwt.secret=mySecretKey
> 
### property for profile
Provided an environement.property variable for showing which profile is chosen.
```shell
environement:
  property: I AM THE DEFAULT | I AM PROD
```

### Log file
The level of log in default mode is DEBUG and in production by default is INFO. 
You can change t in `application` properties file

```shell
logging:
    level:
        ROOT: INFO
        org.hibernate.SQL: INFO
        com.example: DEBUG
```

For production:
```shell
logging:
    level:
        ROOT: INFO
        com.example: INFO
```

### Database
Used a H2 database for default profile

```shell
spring:
    datasource:
        url: jdbc:h2:mem:recipea
        #url: jdbc:h2:file:/data/recipea if you  want to store data, uncomment this line
        driverClassName: org.h2.Driver
        username: sa
        password: sa
    h2:
    console.enabled: true # Access to H2 Console with link: http://localhost:8080/h2-console
    console.path: /h2-console
    console.settings.trace: false
```
Used a MariaDB/MySQL database for production profile. You can change your database here
```shell
spring:
    datasource:
        url: jdbc:mariadb://localhost:3306/recipea
        username: root
        password: root
        driver-class-name: org.mariadb.jdbc.Driver
```

