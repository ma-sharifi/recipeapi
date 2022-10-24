### Assumptions when solving the challenges

* I took this sentence "Created a standalone java application which allows **users** to manage their favourite recipes."  
   that different user have different favourite recipes. For example Alex has his recipe and Mahdi has different list of the recipe. Nobody knows about other recipes.
* Simplicity is more important than other things. Tried to have a small code.
* Assumed user registered, we checked the username and password and found they are a match. then we issued a token.
* Assumed the instruction length is less than 2048 characters.
* There is one instance of the application running at the moment.
* Ingredient endpoint must be protected as well as Recipe
* Pagination fo the result of search is not important.
* Can use native query in the application.