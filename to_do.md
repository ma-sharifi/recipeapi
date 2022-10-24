### What is missing and why
to-do list with things I would add if I have more time and explaining what is missing and why etc

* Save instructions into text search database like ElasticSearch.
* Add another table with name tag instead of fields vegetarian.
* Add paging to the result of query
* Add a parent for each recipe to categorize them by their parent. Put all type chicken recipe under chicken recipe. For example chicken kabab should be under chicken recipe.
  ![hierarchical-recipe-category](https://user-images.githubusercontent.com/8404721/197397795-5079c909-6320-4e18-b8aa-581d6cb4999e.jpg)

* Add more detailed error code and message. Map detailed error to not detailed error code and send to client. It needs more time.
* Use Keycloak as s security server and integrate it with application. Because it increased dependency I did not add it.
* Dockerize application. Did not mention for our project.
* Use Github action for CI/CD. Did not mention for our project.
* If our customers increase we can partition data to different clusters based on username.
* Use Redis as a cache putting ingredient and recipe on it and keep them updated. It helps to don't refer to database a lot.
* Put log to ELK for monitoring (Or Prometheus and Grafana). It takes time to add to our project.
* Add @Loggable annotation for logging requests and response for finding issue in production. for sake of simplicity It did not add it to the code.
* Using HATEOAS for linking resource together. For example after successfull login, service can provide the link of the recipe and ingredient for client in order to use them for next call.
  It helps do not har code url of the resources.
* Add Cucumber for behavioral test
* Use criteria and Specification for creating dynamic query. 

### Why are they missed: 
* Due to the deadline has been set there is not enough time for complete them.
* For simplicity. Each technology you add, it will increase the complexity.
* Due to simplicity I did not define a tag for categorize food. for example tag=vegetarian is just on thing. You can add more tags. like egg, chicken, diet...


