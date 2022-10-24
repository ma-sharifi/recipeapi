# Recipe API
Recipe API assessment for AA bank. This is an assessment of users can organize their recipes.

## Introduction
The problem and its solution are explained.

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

## Solution:
Created a standalone java application that allows **users** to manage their favorite recipes. Each user has own favorite recipe.
I used Java 11, Spring Boot, and Maven to implement this assignment.
A short description of my solution and explaining some design decisions, you can find [here](solution.md)

## Assumption
We have 2 different users. Mahdi and Alex are our users here. Mahdi can manage his list without effect on Alex's favorite list.
My assumptions when solving the challenge, you can find [here](assumptions.md)

### How To run
To clone and run this application, you'll need [Git](https://git-scm.com), [Maven](https://maven.apache.org/), [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html). From your command line:
* A short explanation about how to run the solution with all the needed parts, you can find [here](how_to_run.md)

### Database diagram
For handling this assignment At least we need 2 entities. One use can't have 2 recipes with one title.
![database](https://user-images.githubusercontent.com/8404721/197364029-7958b7f5-7d3a-4606-9c07-90c66f71dc75.jpg)

### Request follow 
Provided three endpoints for handling, recipes, ingredients, and users. First of all user needs to get Bearer token to call other services.
![request-flow-racipea-1](https://user-images.githubusercontent.com/8404721/197363470-8607b704-ae6d-425c-bb4e-1b03548d4e13.jpg)

### How to use/test API
1. Get a Bearer token from /v1/users/login API.
2. Pass this token as a parameter 'Authorization:Bearer Bearer eyJhbGc...' to HTTPie request.
* A file explaining what needs to be done to use the service, please find it [here](how_to_test.md)

### What I missed
There is a to-do list with things I would add if I have more time and explaining what is missing and why. Please find it [here](to_do.md)

## Swagger:
Give your username '/v1/users/login' and get the Bearer Token,Then click on Authorize button at the up right of the page.  Paste the token here. At last click on 'Authorize'
>http://localhost:8080/swagger-ui/index.html

## Test Coverage
Provided 61 tests(Unit test, Integration test, and End-to-end test)
* 100% Class
* 95% Method
* 91% Line

### Provided 4 different ways for test the application:
1. [Swagger](http://localhost:8080/swagger-ui/index.html)
2. Run test `mvn test`
3. Postman. There is a Postman file of this project in this path located [here](postman/recipea.postman_collection.json)
4. CLI. Commands provided with [HTTPie](https://httpie.io/).
*Note: A file explaining what needs to be done to use the service, you can find [here](how_to_test.md)

## How to scale
A description of how it will scale when the number of users grow, you can find [here](scale.md)

## All Documents
* You can find diagrams in path [here](diagram)
* A short description of the solution and explaining some design decisions, you can find [here](solution.md)
* A short explanation about how to run the solution with all the needed parts, you can find [here](how_to_run.md)
* A file explaining what needs to be done to use the service, you can find [here](how_to_test.md)
* A to-do list with things I would add if I have more time and explaining what is missing and why, you can find [here](to_do.md)
* My assumptions when solving the challenge, you can find [here](assumptions.md)
* A description of how it will scale when the number of users grow, you can find [here](scale.md)

