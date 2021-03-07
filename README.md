#Environmental preparation
1. Add necessary dependencies in pom.xml.
2. Start up database and Import data from `sql/lunch-data.sql` to MySql Database.
3. Set MySql as production database in application.properties while set h2 as test database in application-test.properties

#Refactor the structure of the project and database
1. Create com.rezdy.lunch.entity for the entity classes.
2. Transform the database logic from entityManager to JpaRepository in the com.rezdy.lunch.repository.
3. Create com.rezdy.lunch.service.impl for the implementation of services, and the original com.rezdy.lunch.service is for interfaces.
4. Refactor the structure of tables to introduce ID as the primary key.
5. In test folder, com.rezdy.lunch.controller is for controller-layer tests, com.rezdy.lunch.service.impl is for service-layer tests
   and com.rezdy.lunch.repository is for data-access-layer tests.
   
#Assumption
1. Due to the fact that all foods have their expiry date, both BEST_BEFORE and USE_BY in the "ingredient" table have to be "NOT NULL".
2. Correct the date in the ingredient table where "BEST_BEFORE" date should be earlier than "USE_BY" date.
   
#EndPoints
1. GET, /lunch, parameter: date, find recipes whose ingredients are available. 
2. GET, /getRecipe/{title}, find a recipe according to its title, it will return HTTP 404 when the recipe cannot be found.
3. GET, /findRecipes, parameter: excludedIngredient, excludedIngredient is array





# Lunch Microservice

The service provides an endpoint that will determine, from a set of recipes, what I can have for lunch at a given date, based on my fridge ingredient's expiry date, so that I can quickly decide what I’ll be having to eat, and the ingredients required to prepare the meal.

## Prerequisites

* [Java 11 Runtime](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Docker](https://docs.docker.com/get-docker/) & [Docker-Compose](https://docs.docker.com/compose/install/)

*Note: Docker is used for the local MySQL database instance, feel free to use your own instance or any other SQL database and insert data from lunch-data.sql script* 


### Run

1. Start database:

    ```
    docker-compose up -d
    ```
   
2. Add test data from  `sql/lunch-data.sql` to the database. Here's a helper script if you prefer:


    ```
    CONTAINER_ID=$(docker inspect --format="{{.Id}}" lunch-db)
    ```
    
    ```
    docker cp sql/lunch-data.sql $CONTAINER_ID:/lunch-data.sql
    ```
    
    ```
    docker exec $CONTAINER_ID /bin/sh -c 'mysql -u root -prezdytechtask lunch </lunch-data.sql'
    ```
    
3. Run Springboot LunchApplication
