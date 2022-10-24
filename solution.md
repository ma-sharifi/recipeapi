## Solution: a short description of the solution and explaining some design decisions

Created a standalone java application that allows **users** to manage their favorite recipes.
It should allow adding, updating, removing, and fetching recipes.
Additionally, users should be able to filter available recipes based on one or more criteria.

To handle this, I implemented a Spring Boot REST application to handle the criteria.
After users get their bearer token, users can interact with other APIs.
The user needs to create ingredients, and the recipe can then organize them.


### Database diagram
An ingredient can define one time not more than this. I mean we can have one oil for all recipes in my scenario.
I defined a unique constraint on the title of the ingredient.
Users can store just one recipe with a specific title, not more. I defined a unique constraint on the title+username of the recipe entity.
Before saving a new recipe it needs to get the ingredient id, then embed it into your RecipeDto then send the POST request for saving the Recipe.

![database](https://user-images.githubusercontent.com/8404721/197364029-7958b7f5-7d3a-4606-9c07-90c66f71dc75.jpg)

Query and its test were the challenging part of this assessment, I needed to show the recipe without `salmon`and with `potatoes`.
#### For ingredient exclude
At first, I selected the Recipe's ID with salmon in ingredients, then my query must not in this table:
>`ResultSet=(SELECT JND_RECIPE_INGREDIENT.RECIPE_ID FROM JND_RECIPE_INGREDIENT JOIN T_INGREDIENT i ON i.id = JND_RECIPE_INGREDIENT.INGREDIENT_ID  WHERE 1<2 ${WHERE} )` 
 
That ${WHERE} will be replaced with LOWER(i.title) =? `salmon`
And `${NOT}` will be replaced with `not` in the following script:
> `SELECT * FROM T_RECIPE r WHERE r.id ${NOT} in (ResultSet) AND 1<2`
* It will be turned to: 
> `SELECT * FROM T_RECIPE r WHERE r.id NOT in (ResultSet) AND 1<2`

To test them after getting the result, I used following filter to find how many recipe I have without matching with `salmon` in its ingredients.
> long sizeNonSalmonRecipeObtainFromProcessingTheResponseOfTheAPIcall= responseDto.getPayload().stream().filter(recipe -> recipe.getIngredients().stream().noneMatch(ingredientDto -> ingredientDto.getTitle().contains("salmon"))).count();
* In assert I test the size of response of my stream process with size of query obtained from repository.
> assertEquals(sizeNonSalmonRecipeObtainFromProcessingTheResponseOfTheAPIcall,resultOfRepository.size());

#### For ingredient include(Recipe with potatoes ingredient), 
I selected the Recipe's ID with `potatoes` in ingredients as well, then my query must be in this table the same as without salmon.
But needed to replace `${NOT}` with `""` , because we want our recipe have `potatoes` in its ingredients.
> `SELECT * FROM T_RECIPE r WHERE r.id ${NOT} in (ResultSet) AND 1<2`
* It will be turned to: 
> `SELECT * FROM T_RECIPE r WHERE r.id in (ResultSet) AND 1<2`

* To test them after getting the result, I used following filter to find how many recipe I have with any matching with `potatoes` in its ingredients.

>long sizePotatoRecipeObtainFromProcessingTheResponseOfTheAPIcall= responseDto.getPayload().stream().filter(recipe -> recipe.getIngredients().stream().anyMatch(ingredientDto -> ingredientDto.getTitle().contains("potatoes"))).count();
* In assert I test the size of response of my stream process with size of query obtained from repository.
>assertEquals(resultOfRepository, sizePotatoRecipeObtainFromProcessingTheResponseOfTheAPIcall);
* You can find them in test/controller/RecipeControllerIT test.

### ResponseDto
There is a ResponseDto object. our response consist of this object. This object has a List<T> payload.
Client **MUST** find the body of the response here. Due to simplicity for client, payload is always a list. As a result, client just need process this field.
We know an object is an array with a length of one.

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

#### Endpoints
**v1/recipes**
User can add, update, remove and fetch recipes

**v1/ingredients**
User can add and fetch ingredients

**v1/users/login**
Issue token
* Note:
  1. Assumed user already registered and we persist them.
  2. Assumed we checked the user and password and found they are a match. then we issued a token.
  3. This endpoint is not protected.
1. We issue a JWT token and set the subject our user.
2. Put the Bearer token into body(for simplicity) and Authorization HTTP Header.
3. Return 200 if everything goes well.

### Security
All APIs protect by Bearer token except /login. For simplicity is used Spring Security. For production, I will use Keycloak.
I used a facade for authentication. It helps to retrieve the authentication everywhere,not just in @Controller beans.
Also This facade encapsulates a complex subsystem behind a simple interface. It defines a higher-level interface that makes the subsystem easier to use.
In the future we can add Keycloak to our project as well.

###Test
* Different type of test provided.
* Provided a solitary test for RecipeService. I mocked the database and just test functionality.
* You can test the application 4 different ways. Swagger, Postman, mvn Test, HTTPie CLI. 

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

### For sake of simplicity:
* Used Spring Security. And assumed the user data already persisted in database.
* I provided vegetarian as a boolean in Recipe entity. If I had defined it as an entity, it would have taken longer and the code would have been more complex.
