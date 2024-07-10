# REST API Example

This repository contains an implementation of REST API using [Jakarta EE](https://jakarta.ee/specifications/restful-ws/)
for the DietiDeals application

--------------------------------------

## Running the REST API with Docker
To build a docker image, run
```
docker build -t "dd-rest-api-image" .
```
To run the image, run
```
docker run -p 9000:8080 -it --name dd-rest-api-container dd-rest-api-image
```
The REST API will be available at [http://localhost:9000/](http://localhost:9000/).