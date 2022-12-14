# How to test service
It explains what needs to be done to test the service

###You can test this project 4 different ways:
1. [Swagger](http://localhost:8080/swagger-ui/index.html)
2. Run test `mvn test`
3. Postman. There is a Postman file of this project in this path located [here](postman/recipea.postman_collection.json)
4. CLI. Commands provided with [HTTPie](https://httpie.io/).

### API
I described all APIs here. All APIs are protected except /v1/users that I used it for issuing token for other APIS.

#### 1. User /v1/users
1. Issue a bearer token.
* **POST**`/v1/users/login` HTTP Status: 200
* Note: API get the username from this token and it effects on its queries. Queries of recipe have username as parameter.
* Note: For test different user, you can provide 2 different Bearer Token (JWT) with different username.

#### 2. Recipe /v1/recipes
1. Users can create(add) recipe.
 * **POST**`/v1/recipes` HTTP Status: 201
 *Note: For post because I put the URL of the created entity in the Location HTTP Header, we can remove the body from the response.
2. Get list of all recipes of current user.
 * **GET**`/v1/recipes` HTTP Status: 200
 * Provided by paging. You can use it this way in this service: ?sort=id,desc&page=0&size=2
 * `sort=id,desc` it means sort by id by descending order (use asc for ascending). Id is the field of recipe.
 * size=2 it means paginate the result such a way there are 2 items per page.
 * page=0 just return page 0 not any other pages.
3. Get user recipe by its `id`.
 * **GET**`/v1/recipes/{id}` HTTP Status: 200
4. Delete a recipe of user 
* **DELETE**`/v1/recipes` HTTP Status: 204
5. Search into user recipe with given criteria
* **GET**`/v1/recipes/search` HTTP Status: 200

#### 3. Ingredient /v1/ingredients
*Note: Ingredients are share between users. Each user can use them.
1. Users can create(add) ingredient
* **POST**`/v1/ingredients` HTTP Status: 201
2. Get list of all ingredients
* **GET**`/v1/ingredients` HTTP Status: 200
3. Get a ingredient by its `id`.
* **GET**`/v1/ingredients/{id}` HTTP Status: 200
 
###How to use API
1. Get an Bearer token from /v1/users/login API.
2. Pass this token as a parameter 'Authorization:Bearer Bearer abcxwy...' to HTTPie reqest.

## Swagger:
For using swagger first of all, you need to issue a token. Then apply this token to swagger. All APIs have example. For sake of simplicity you can use them.
Call API '/v1/users/login' with your username and get the Bearer Token, Then click on Authorize button at the up right of the swagger main page.  Paste the token here. At last click on 'Authorize'. You can try other APIs now.
>http://localhost:8080/swagger-ui/index.html

## Postman
Provided different request for different user Mahdi and Alex. I categorized them by folder. You need just import this file into your Postman.
There is a Postman file of this project in this path located [here](postman/recipea.postman_collection.json)

## HTTPie

### /v1/users/login
* Get a bearer token
  **POST**`/v1/users/login`

#### Request:
```bash
  http --form POST localhost:8080/v1/users/login username=mahdi
```
#### Successful response:
```bash
HTTP/1.1 200
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTM3MzMzLCJleHAiOjE2NjkxMzI5MzN9.cfzE-SuRIi1ClUi7a_O3AkKZBHUCapHapZZi1veLcas3c1iXu88f_RjQTY6sFwH_237pHmwEIPqparhf6qm6Rg
```

```json
{
  "token": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4MjQzLCJleHAiOjE2NjkxMTM4NDN9._oXNQgqgOFaRLieLYq9cSLkreFeFz4XyeI9w5e_Cu8J2cUlSQUMg_urvG07Nh1o4Ufk3jQkon1i9qW8iLKbYBw",
  "user": "mahdi"
}
```
### POST /v1/recipes
1. Users can create(add) recipe
* **POST**`/v1/recipes` HTTP Status: 201
#### Request:
```bash
http POST localhost:8080/v1/recipes title="sushi" instruction="cooked by stove" serve=2 vegetarian=false 'ingredients:=[{"id":2}]' 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
*Note: With HTTPie you can send raw JSON this way: `field:=json`. 
For example:  
```bash
ingredients:=[{"id":2}]
```

#### Successful response:
The header and Location is enough for this API. We can remove the reponse body. If client needs, It can get url of the just created entity from `Location` in HTTP Header.
```bash
HTTP/1.1 201
Location: /v1/recipes/6
```
```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 6,
      "ingredients": [
        {
          "id": 2
        }
      ],
      "instruction": "cooked by stove",
      "serve": 2,
      "title": "sushi",
      "username": "mahdi",
      "vegetarian": false
    }
  ],
  "timestamp": "2022-10-23T10:45:04.078+00:00"
}
```

### GET /v1/recipes
Get list of all user recipes HTTP Status: 200
#### Request:
```bash
http GET localhost:8080/v1/recipes 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
#### Successful response:
```bash
HTTP/1.1 200
```
```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 4,
      "ingredients": [
        {
          "id": 1,
          "title": "oil"
        },
        {
          "id": 2,
          "title": "salmon"
        }
      ],
      "instruction": "This kabab cooked by stove",
      "serve": 1,
      "title": "Salmon Kabab",
      "username": "mahdi",
      "vegetarian": false
    },
    {
      "id": 5,
      "ingredients": [
        {
          "id": 1,
          "title": "oil"
        },
        {
          "id": 3,
          "title": "potatoes"
        }
      ],
      "instruction": "Cook potato on oven",
      "serve": 4,
      "title": "French fries",
      "username": "mahdi",
      "vegetarian": true
    },
    {
      "id": 6,
      "ingredients": [
        {
          "id": 2,
          "title": "salmon"
        }
      ],
      "instruction": "cooked by stove",
      "serve": 2,
      "title": "sushi",
      "username": "mahdi",
      "vegetarian": false
    }
  ],
  "timestamp": "2022-10-23T10:45:26.081+00:00"
}
```

### GET /v1/recipes/{id}
Get a user recipe by its `id`. HTTP Status: 200
Get the id from response of POST /v1/recipes
#### Request:
```bash
http GET localhost:8080/v1/recipes/6 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
#### Successful response:
```bash
HTTP/1.1 200
```
```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 6,
      "ingredients": [
        {
          "id": 2,
          "title": "salmon"
        }
      ],
      "instruction": "cooked by stove",
      "serve": 2,
      "title": "sushi",
      "username": "mahdi",
      "vegetarian": false
    }
  ],
  "timestamp": "2022-10-23T10:47:02.784+00:00"
}
```

### DELETE /v1/recipes/{id}
Delete a user recipe by specified `id`. HTTP Status: 204
Get the id from response of POST /v1/recipes
#### Request:
```bash
http DELETE localhost:8080/v1/recipes/6 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
#### Successful response:
```bash
HTTP/1.1 204
```
With empty response body.

#### Response of call again delete request:
```bash
HTTP/1.1 404
```
```json
{
    "details": "No class com.example.recipea.entity.Recipe entity with id 6 exists!",
    "error_code": 4043,
    "message": "#This entity does not exists!!",
    "timestamp": "2022-10-23T10:53:00.824+00:00"
}
```

### GET /v1/recipes/search
Search user recipe with given criteria. HTTP Status: 200
1. find all vegetarian recipe
2. find all none-vegetarian recipe
3. find by number of serving
4. find all recipes for 4 person with potatoes
5. Request find all recipes without salmon as ingredient with oven as instruction

```bash
HTTP/1.1 200 
```
#### 1. Request find all vegetarian recipe
```bash
http GET "localhost:8080/v1/recipes/search?isveg=true" 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
#### Response successful find all vegetarian recipe

```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 5,
      "ingredients": [
        {
          "id": 1,
          "title": "oil"
        },
        {
          "id": 3,
          "title": "potatoes"
        }
      ],
      "instruction": "Cook potato on oven",
      "serve": 4,
      "title": "French fries",
      "username": "mahdi",
      "vegetarian": true
    }
  ],
  "timestamp": "2022-10-23T10:58:30.060+00:00"
}
```
#### 2. Request find all none-vegetarian recipe
```bash
http GET "localhost:8080/v1/recipes/search?isveg=false" 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
#### Response successful find all none-vegetarian recipe
```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 4,
      "ingredients": [
        {
          "id": 1,
          "title": "oil"
        },
        {
          "id": 2,
          "title": "salmon"
        }
      ],
      "instruction": "This kabab cooked by stove",
      "serve": 1,
      "title": "Salmon Kabab",
      "username": "mahdi",
      "vegetarian": false
    }
  ],
  "timestamp": "2022-10-23T11:02:24.962+00:00"
}
```
#### 3. Request find by number of serving
```bash
http GET "localhost:8080/v1/recipes/search?serve=4" 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
#### Response successful find by number of serving

```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 5,
      "ingredients": [
        {
          "id": 1,
          "title": "oil"
        },
        {
          "id": 3,
          "title": "potatoes"
        }
      ],
      "instruction": "Cook potato on oven",
      "serve": 4,
      "title": "French fries",
      "username": "mahdi",
      "vegetarian": true
    }
  ],
  "timestamp": "2022-10-23T11:04:48.557+00:00"
}
```

#### 4. Request find all recipes for 4 person with potatoes
```bash
http GET "localhost:8080/v1/recipes/search?ingredient=potatoes&serve=4" 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
#### Response successful find all recipes for 4 person with potatoes

```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 5,
      "ingredients": [
        {
          "id": 1,
          "title": "oil"
        },
        {
          "id": 3,
          "title": "potatoes"
        }
      ],
      "instruction": "Cook potato on oven",
      "serve": 4,
      "title": "French fries",
      "username": "mahdi",
      "vegetarian": true
    }
  ],
  "timestamp": "2022-10-23T11:07:10.781+00:00"
}
```


#### 5. Request find all recipes without salmon as ingredient with oven as instruction
```bash
http GET "localhost:8080/v1/recipes/search?ingredient=-salmon&instruction=oven" 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
#### Response successful find all recipes without salmon as ingredient with oven as instruction

```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 5,
      "ingredients": [
        {
          "id": 1,
          "title": "oil"
        },
        {
          "id": 3,
          "title": "potatoes"
        }
      ],
      "instruction": "Cook potato on oven",
      "serve": 4,
      "title": "French fries",
      "username": "mahdi",
      "vegetarian": true
    }
  ],
  "timestamp": "2022-10-23T11:08:32.077+00:00"
}
```


#### Ingredient /v1/ingredients
1. Users can create(add) ingredient
* **POST**`/v1/ingredients` HTTP Status: 201
2. Get list of all ingredients
* **GET**`/v1/ingredients` HTTP Status: 200
3. Get a ingredient by its `id`.
* **GET**`/v1/ingredients/{id}` HTTP Status: 200


### 1. POST /v1/ingredients
1. Users can create(add) ingredient
* **POST**`/v1/ingredients` HTTP Status: 201
* Note: Can remove the response body. Because I provided the URL of the just created resource into Location field of HTTP Header.
#### Request:
For this request I used **--raw JSON** in order to manage to send raw JSON with HTTPie
```bash
http --raw '{"title": "salt"}' POST localhost:8080/v1/ingredients 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```

#### Successful response:
The header and Location is enough for this API. We can remove the response body. If client needs, It can get url of the just created entity from `Location` in HTTP Header.
```bash
HTTP/1.1 201
Location: /v1/ingredients/8
```
```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 8,
      "title": "salt"
    }
  ],
  "timestamp": "2022-10-23T11:17:05.890+00:00"
}
```

### 2. GET /v1/ingredients
Get list of all ingredients HTTP Status: 200
#### Request:
```bash
http GET localhost:8080/v1/ingredients 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
#### Successful response:
```bash
HTTP/1.1 200
```
```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 1,
      "title": "oil"
    },
    {
      "id": 2,
      "title": "salmon"
    },
    {
      "id": 3,
      "title": "potatoes"
    },
    {
      "id": 8,
      "title": "salt"
    }
  ],
  "timestamp": "2022-10-23T11:19:18.652+00:00"
}
```

### 3. GET /v1/ingredients/{id}
Get a ingredient by its `id`. HTTP Status: 200
Get the id from response of POST /v1/ingredients
#### Request:
```bash
http GET localhost:8080/v1/ingredients/8 'Authorization:Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2NTE4ODk5LCJleHAiOjE2NjkxMTQ0OTl9.zBrMxevBPatKwirCF_OmwpAeusxB63ze5yy_RkefqgKZnRAuxy7y-IYIvDjznTdpgwrxqvXuXLzs7GnKhF6m_w'
```
#### Successful response:
```bash
HTTP/1.1 200
```
```json
{
  "error_code": 0,
  "message": "Success",
  "payload": [
    {
      "id": 8,
      "title": "salt"
    }
  ],
  "timestamp": "2022-10-23T11:19:53.542+00:00"
}
```
