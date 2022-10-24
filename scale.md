### How we can scale the application?
Describe how it will scale as the number of users grows each day, and what changes will be needed to maintain quality

* Use Redis as a cache putting ingredient and recipes into it and keep them for a period of time.
* Based on Scale Qube, we can partition data in our database (scale by splitting similar things) to different clusters.
* Dockerize application for deploy on any cloud native platform.
* Use a separate product for security. Use Keycloak as s security server (Decomposition of /v1/user from the current app) and integrate it with the application and deploy it on a cloud.
* Put log to ELK for monitoring (Or Prometheus and Grafana) for finding issue before they become serious in production.
* Use a cloud database.
* Deploy application on Azure App Service.
* Add CI/CD pipeline for faster deployment.

