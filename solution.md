## Solution: a short description of the solution and explaining some design decisions

Ceated a standalone java application which allows **users** to manage their favourite recipes.
It should allow adding, updating, removing and fetching recipes.
Additionally, users should be able to filter available recipes based on one or more criteria.

For handling this, I implemented a Spring Boot REST application to handle the criteria.
After user get their bearer token, user can interact with other APIs.
User needs to create ingredients and recipe then can manipulate them.
An ingredient can define one time not more than this. I mean we can have one oil for all recipes in my senario. 
I defined a unique constraint on title of ingredient.
User can store just one recipe with a specific title not more. I defined a unique constraint on title+username of recipe entity.
Before saving a new recipe it needs to get ingredient id, then embed it into you RecipeDto then send your POST request for saving Recipe.
The diagram shown below:

### For sake of simplicity:
* I did not check recipe is belong to current user for delete and update it. I assumed current user is admin and can do everything.
* User can see every other recipe belong to other user. If you dont want to see just add AND username = ?username to the query.
* Used Spring Security. And assumed the user data already persisted in database.
* I provided vegetarian as a boolean in Recipe entity. If I had defined it as an entity, it would have taken longer and the code would have been more complex.

### ResponseDto
There is a ResponseDto object. our response consist of this object. This object has a List<T> payload.
Client **MUST** find the body of the response here. Due to simplicity for client, payload is always a list. As a result, client just need process this field.
We know an object is an array with a length of one.

### Database diagram

![database](https://user-images.githubusercontent.com/8404721/197364029-7958b7f5-7d3a-4606-9c07-90c66f71dc75.jpg)

Query was the challenging part of this assessment, I need to show the recipe without salmon. 
At first I select the recipe with salmon, then my query must not in this table. Other part was as usual. 

### API design

#### Endpoint to service diagram

![flow-endpoint-service](https://user-images.githubusercontent.com/8404721/197379567-355766d2-c7f0-4a76-8c32-20bb9683e6d7.jpg)

#### v1/recipes/search
I provided a minus(-) before ingredient for excluding ingredient. For recipe without salmon you need to send request like `ingredient=-salmon` in query string, to exclude this ingredient.
For recipe include potatoes you need to send this request `ingredient=potatoes`.
* Whether the dish is vegetarian: provided by a `&isveg=true` query param. It will return all vegetarian.
* The dish is not vegetarian: provided by a `&isveg=false` query param. It will return all none vegetarian.
* I use `isveg` not `isVeg` for sake of uniform design. All query parameter must be in lower case. All queries will be done with LOWERCAE.
* The number of servings: provided by a `&serve=4` query param. It will return all recipes can serve by 4 people.
* Specific ingredients include: provided by a `&ingredient=salmon` query param. It will return all recipes with this ingredient.
* Specific ingredients exclude: as I mentioned a `&ingredient=-salmon` query param. It will return all recipes without this ingredient.
* Text search within the instructions: provided by a `&instruction=oven` query param.
* Recipes that can serve 4 persons and have “potatoes” as an ingredient: provided by `?serve=4&ingredient=potatoes`
* Recipes without “salmon” as an ingredient that has “oven” in the instructions: provided by `?ingredient=-salmon&instruction=oven`

#### HTTP Status
1. If the result of `GET` (/search,recipes/1,/recipes) be success HTTP Status 200 and error_code 0.
2. If the result of POST (create) be success, the response will contain HTTP Status 201 and error_code 0 and a Location with the url of the newly created entity in header (HTTP Header-> Location:/v1/recipes/1).
3. If something be not normal from the client side a HTTP Status 400 (Bad Request) will be return.
4. If the entity was not found HTTP Status will be 404 (Not Found).
5. If something unhandle accoured in server side the HTTP Status will be 500.

### Security
All APIs protect by Bearer token except /login. For simplicity is used Spring Security. For production, I will use Keycloak.
I used a facade for authentication. It helps to retrieve the authentication everywhere,not just in @Controller beans.
Also This facade encapsulates a complex subsystem behind a simple interface. It defines a higher-level interface that makes the subsystem easier to use.
In the future we can add Keycloak to our project as well.

###Test
Different type of test provided.
You can test the application 4 different ways. Swagger, Postman, mvn Test, HTTPie CLI. 

### Exception
Defined different Exceptions for different situations.
Provided a [Global Exception](src/main/java/com/example/recipea/exception/globalhandler/GlobalExceptionHandler.java) handler to help handle exceptions in an easy way.
In `ResponseDto` there is a field with name `error_code` you can see error code of each request. 
Global Exception, put the error_code into `ResponseDto` base the condition.
Provided a `message` to show the text result of request.
If `log debug mode` is activated, you can see the `details` of the error in the response.
The sample response of a result with eror at below:
```json
{
   "message": "#Unique index or primary key violation!",
   "details": "Unique index or primary key violation: \"PUBLIC.UNQ_TITLE_USERNAEM_INDEX_4 ON PUBLIC.T_RECIPE(TITLE, USERNAME) VALUES 3\"; SQL statement:\ninsert into t_recipe (id, instruction, serve, title, username, vegetarian) values (default, ?, ?, ?, ?, ?) [23505-199] ;insert into t_recipe (id, instruction, serve, title, username, vegetarian) values (default, ?, ?, ?, ?, ?)",
   "timestamp": "2022-10-21T15:54:38.198875",
   "error_code": 4005
}
```
Provided the exceptions below;
* BadRequestException
* RecipeNotFoundException
* IngredientNotFoundException

### Mapping
I used MapStruct for mapping entity to Dto and vice versa. Mapstruct is compile time not runtime. It helps to have a better speed.

####Notes:
* Due to the lack of Category in Omdb API, first of all file was checked for "Best Picture".
* Content of OMDB API: "Title": "Inception", "Awards": "Won 4 Oscars. 157 wins & 220 nominations total",
* Content of CSV FILE: 2010 (83rd),Best Picture,The King's Speech,"Iain Canning, Emile Sherman and Gareth Unwin, Producers",YES,,,,,,

**v1/recipes**
User can add, update, remove and fetch recipes

**v1/ingredients**
User can add, update, and fetch ingredients

**v1/users/login**
Issue token
* Note:
    1. Assumed user already registered and we persist them.
    2. Assumed we checked the user and password and found they are a match. then we issued a token.
    3. This endpoint is not protected.
1. We issue a JWT token and set the subject our user.
2. Put the Bearer token into body(for simplicity) and Authorization HTTP Header.
3. Return 200 if everything goes well.