# Recipe API
Recipe API assessment for AA bank. This is an assessment of users can organize their recipes.

## Introduction
Welcome to Recipe A.

### Objective - The main features of this Recipe application:
Create a standalone java application which allows users to manage their favourite recipes. It should
allow adding, updating, removing and fetching recipes. Additionally users should be able to filter
available recipes based on one or more of the following criteria:
1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.
   For example, the API should be able to handle the following search requests:
   • All vegetarian recipes
   • Recipes that can serve 4 persons and have “potatoes” as an ingredient
   • Recipes without “salmon” as an ingredient that has “oven” in the instructions.

### Requirements
Please ensure that we have some documentation about the architectural choices and also how to
run the application. The project is expected to be delivered as a GitHub (or any other public git
hosting) repository URL.
All these requirements needs to be satisfied:
1. It must be a REST application implemented using Java (use a framework of your choice)
2. Your code should be production-ready.
3. REST API must be documented
4. Data must be persisted in a database
5. Unit tests must be present
6. Integration tests must be present

# Assumption
* I assumed the intstruction length is less than 2048 character.
* Assumed user registered, we checked the username and password and found they are a match. then we issued a token.

### Initial Configuration
1.	Apache Maven (http://maven.apache.org)  This code have been compiled with Java version 11.
2.	Git Client (http://git-scm.com)

### How To Use
To clone and run this application, you'll need [Git](https://git-scm.com), [Maven](https://maven.apache.org/), [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html). From your command line:
* A short explanation about how to run the solution with all the needed parts, you can find [here](how_to_run.md)

### Database diagram

![database](https://user-images.githubusercontent.com/8404721/197364029-7958b7f5-7d3a-4606-9c07-90c66f71dc75.jpg)


### Request follow 

![request-flow-racipea-1](https://user-images.githubusercontent.com/8404721/197363470-8607b704-ae6d-425c-bb4e-1b03548d4e13.jpg)

### ResponseDto
There is a ResponseDto object. our response consist of this object. This object has a List<T> payload.
Client **MUST** find the body of the response here. Due to simplicity for client, payload is always a list. As a result, client just need process this field.
We know an object is an array with a length of one.

### Exception
Defined different Exceptions for different situations.
Provided a Global Exception handler to help handle exceptions in an easy way.

### Mapping
I used MapStruct for mapping entity to Dto and vice versa. Mapstruct is compile time not runtime. It helps to have a better speed.

### Logg and debugging
If `log debug mode` is activated, you can see the `details` of the error in the response.
* Notes: Do not active `log debug mode` in the production. By default it is deactivated.
```json
{
   "message": "#Unique index or primary key violation!",
   "details": "Unique index or primary key violation: \"PUBLIC.UNQ_TITLE_USERNAEM_INDEX_4 ON PUBLIC.T_RECIPE(TITLE, USERNAME) VALUES 3\"; SQL statement:\ninsert into t_recipe (id, instruction, serve, title, username, vegetarian) values (default, ?, ?, ?, ?, ?) [23505-199] ;insert into t_recipe (id, instruction, serve, title, username, vegetarian) values (default, ?, ?, ?, ?, ?)",
   "timestamp": "2022-10-21T15:54:38.198875",
   "error_code": 4005
}
```

###How to use API
1. Get an Bearer token from /v1/users/login API.
2. Pass this token as a parameter 'Authorization:Bearer Bearer eyJhbGc...' to HTTPie request.

## Swagger:
Give your username '/v1/users/login' and get the Bearer Token,Then click on Authorize button at the up right of the page.  Paste the token here. At last click on 'Authorize'
>http://localhost:8080/swagger-ui/index.html

## HTTPie
###/v1/users/login
* Get a bearer token
  **POST**`/v1/users/login`
```bash
  http --form POST localhost:8080/v1/users/login username=mahdi
```
Result:
```json
{
    "token": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJtb3ZpZWFwaSIsInN1YiI6Im1haGRpIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY2NTQzNzgxMywiZXhwIjoxNjY4MDMzNDEzfQ.xtuu2sriJZFI0_Q__OYr-aK1vPJkyiUjMo0F-k0E6lnJR0AO3xagz6bT3WqZVIopTl_VZRBDvqlPZxGQijvnyg",
    "user": "mahdi"
}
```

## Test Coverage
* 100% Class
* 96% Method
* 93% Line

## Other Documents
* You can find diagrams in path [here](diagram)
* A short description of the solution and explaining some design decisions, you can find [here](solution.md)
* A short explanation about how to run the solution with all the needed parts, you can find [here](how_to_run.md)
* A file explaining what needs to be done to use the service, you can find [here](how_to_test.md)
* A to-do list with things I would add if I have more time and explaining what is missing and why, you can find [here](to_do.md)
* My assumptions when solving the challenge, you can find [here](assumptions.md)
* A description of how it will scale when the number of grows, you can find [here](scale.md)


#Future
* needs to save instruction into text search database like ElasticSearch,
* Add tag table instead of fields vegetarian.
* Add paging to the result of query
* Add @Loggable annotation for logging requests and response for finding issue in production. for sake of simplicity It did not add it to the code. 
