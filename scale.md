### How we can scale the application?
a description of how it will scale when the number of users grows per day, and what changes would have to be made to keep the same quality of service

* Use Redis as a cache putting ingredient and recipes into it and keep them for a period of time.
* Based on Scale Qube, we can partition data in our database (scale by splitting similar things) to different clusters.
* Dockerize application for deploy on any cloud native platform.
* Use a separate server for security. Use Keycloak as s security server (Decomposition of /v1/user from the current app) and integrate it with the application and deploy it on a cloud.
* Put log to ELK for monitoring (Or Prometheus and Grafana) for finding issue before they become serious.
* Use a cloud database.
* Deploy application on Azure App Service.
* Add CI/CD pipeline.

