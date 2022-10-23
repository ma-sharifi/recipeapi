### What is missing and why
to-do list with things I would add if I have more time and explaining what is missing and why etc

* Add another table with name tag
* Add a parent for each recipe to categorize them by their parent. Put all type chicken recipe under chicken recipe. For example chicken kabab should be under chicken recipe.
* Add more detailed error code and message. Map detailed error to not detailed error code and send to client. It needs more time.
* Use Keycloak as s security server and integrate it with application. Because it increased dependency I did not add it.
* Dockerize application. Did not mention for our project.
* Use Github action for CI/CD. Did not mention for our project.
* If our customers increase we can partition data to different clusters based on username.
* Use Redis as a cache putting ingredient and recipe on it and keep them updated. It helps to don't refer to database a lot.
* Put log to ELK for monitoring (Or Prometheus and Grafana). It takes time to add to our project.

### Why are they missed: 
* Due to the deadline has been set there is not enough time for complete them.
* For simplicity. Each technology you add, it will increase the complexity.
* Due to simplicity I did not define a tag for categorize food. for example tag=vegetarian is just on thing. You can add more tags. like egg, chicken, diet...


