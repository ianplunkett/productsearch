# Product Search Demo
This api provides an interface to build a new lucene index of the product listing supplied by: 

https://mobile-tha-server.firebaseapp.com/.

And provides an interface to query and filter the product listing based on the following criteria: 

* search 
* minPrice 
* maxPrice 
* minReviewRating 
* maxReviewRating 
* minReviewCount 
* maxReviewCount 
* inStock


## Build Instructions

To build the project locally you need `gradle 4.10.2` and `java 1.8`

At the root level of the project run 
```
./gradlew bootJar
java -jar build/libs/productsearch-0.0.4-SNAPSHOT.jar
```

To run tests
```
./gradlew test
```

To run the project as a docker container
```
docker build -t productsearch:0.0.4-SNAPSHOT .
docker run -p 127.0.0.1:8080:8080/tcp productsearch:0.0.4-SNAPSHOT
```

## Initializing the index

Before you can search against the api you *must* issue a `PUT` request to `/indexer`.  When the api initially loads, the index is not initialized and calls to the search endpoint will result in 500 errors.

## Postman

To see the api in action there Postman examples here: 

https://documenter.getpostman.com/view/5625699/SWLfaS7k?version=latest

Alternatively, you get issue `GET` requests directly against the api:

http://35.202.217.40/search?maxPrice=$20.00&minPrice=$0.00&minReviewRating=4.5&maxReviewRating=5&minReviewCount=1&maxReviewCount=500&inStock=true&q=tv%20protection
