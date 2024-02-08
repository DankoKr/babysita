# Babysita

Spring Boot server that was implemented during my third semester at Fontys.

## Technologies used

The suggested IDE for this project is [IntelliJ](https://www.jetbrains.com/idea/)

- [Java Spring Boot](https://spring.io/projects/spring-boot) framework for Java API
- [Gradle](https://gradle.org/) Build tool for dependancies and environment
- [MySQL](https://www.mysql.com/)
- [Docker](https://www.docker.com/)
- JWT token for Authentication and Authorization

## 📂 Project Structure

The project structure layout:

```python
babysita-web-main/
  ├── documents/                          # Project documents
  ├── gradle/                             # Gradle wrapper
  ├── src/main/java/s3.fontys.babysita    # Application source code
  │   ├── business/                       # Business related classes
  │   │   └── exceptions/                 # Exceptions classes
  │   │   └── impl/                       # Implementation of the service interface classes
  │   │   └── mapper/                     # Mapper for the classes
  │   ├── configuration/                  # The files responsible for the authentication and the exception handler class
  │   │   └── exceptionhandler/           # Exception handler
  │   │   └── security/                   # Folder with all the authentication logic (Token, SecurityConfig)
  │   │   └── WebSocketConfig             # Configuration for Websockets
  │   ├── controller/                     # Controller classes
  │   ├── domain/                         # All classes for Request/Response
  │   ├── dto/                            # Dto classes
  │   ├── persistance/                    # Database related classes and interfaces
  │   │   └── entity/                     # Entity classes
  ├── src/test/java/s3.fontys.babysita/   # This is the mirrored main structre but for tests
  ├── .gitlab-ci.yml                      # Pipeline setup
  ├── build.gradle                        # Gradle dependancies and packages
  ├── Dockerfile                          # File for generating a docker image
  ├── settings.gradle                     # Gradle settings
  ├── wait-for-db.sh                      # Script for the db that is used in the Dockerfile
  ├── gradlew.bat
  └── gradlew
```

## Testing

Testing is done using [Mockito](https://site.mockito.org/) and the [Spring Boot testing](https://www.baeldung.com/spring-boot-testing).

## Routes

# Permission is requireed to all endpoints except the one below. In order to get such you need to send a Bearer token.

# Get the token by login in

```
curl -X POST -H "Content-Type: application/json" -d '{
    "username": "",
    "password": "",
}' http://example.com/tokens
```

As a response you get the Bearer token:

```json
{
  "accessToken": ""
}
```

# Endpoints for user

## Get all users - admin only

```
curl -X GET http://example.com/users
```

You get a list of all of the users in the following format :

```json
{
    "1": {
        "id": 1,
        "username": "",
        "email": "",
        "firstName": "",
        "lastName": "",
        "profileImage": "",
        "phoneNumber": "",
        "address": "",
        "role": "",
        "age":
    },
    "2": {
        "id": 2,
        "username": "",
        "email": "",
        "firstName": "",
        "lastName": "",
        "profileImage": "",
        "phoneNumber": "",
        "address": "",
        "role": "",
        "age":
    },
}
```

## Get a user via the user Id - admin and the a given user for their data only

```
curl -X GET http://example.com/users/1
```

You get the user in the following json

```json
{
    "id": 1,
    "username": "",
    "email": "",
    "firstName": "",
    "lastName": "",
    "profileImage": "",
    "phoneNumber": "",
    "address": "",
    "role": "",
    "age":
}
```

## Get a user via the user username - admin and parent

```
curl -X GET http://example.com/users/search/username
```

You get the user in the following json

```json
{
    "id": 1,
    "username": "username",
    "email": "",
    "firstName": "",
    "lastName": "",
    "profileImage": "",
    "phoneNumber": "",
    "address": "",
    "role": "",
    "age":
}
```

## Create a user - everyone

```
curl -X POST -H "Content-Type: application/json" -d '{
    "username": "",
    "password": "",
    "email": "",
    "firstName": "",
    "lastName": "",
    "profileImage": "",
    "phoneNumber": "",
    "address": "",
    "role": "",
    "age": ,
    "gender": ""
}' http://example.com/users/
```

You get 204 response.

## Update one data for a user - admin or a given user

```
curl -X PATCH -H "Content-Type: application/json" -d '{
    "atrribute_name": ""
}' http://example.com/users/1
```

###

As a return you get response 204.

## Delete a user - admin or a given user

If you need to delete a user just :

```
curl -X DELETE http://example.com/users/1
```

As a return you get response 204.

# Endpoints for posters

## Get all posters and posters without a babysitter - babysitter only

Without a babysitter:

```
curl -X GET http://example.com/posters/noBabysitter
```

For all:

```
curl -X GET http://example.com/posters
```

You get the list of all of the posters without a babysitter in the following format :

```json
{
  "1": {
    "id": 1,
    "title": "",
    "description": "",
    "imageUrl": "",
    "eventDate": "2023-11-10",
    "parentId": 0,
    "babysitterId": 0
  },
  "2": {
    "id": 2,
    "title": "",
    "description": "",
    "imageUrl": "",
    "eventDate": "2023-11-29",
    "parentId": 0,
    "babysitterId": 0
  }
}
```

## Create a poster - parent only

```
curl -X POST http://example.com/offices/
```

You can create a poster that requires the following body :

```json
{
  "title": "",
  "description": "",
  "imageUrl": "",
  "eventDate": "",
  "parentId": 1
}
```

## Get a specific poster via the poster id - everyone

```
curl -X GET http://example.com/posters/1
```

You get all info of the specified poster

```json
{
    "id": 1,
    "title": "",
    "description": "",
    "imageUrl": "",
    "eventDate": "2023-11-29",
    "parentId": 0,
    "babysitterId": 0
},
```

## Update the babysitterId for a poster - parent only

```
curl -X PATCH -H "Content-Type: application/json" -d '{
  "babysitterId": 1,
}' http://example.com/posters/2
```

And as a response you get "Poster successfully assigned to babysitter.".

## Update the poster information - parent and admin

```
curl -X PUT -H "Content-Type: application/json" -d '{
    "title": "",
    "description": "",
    "imageUrl": "",
    "eventDate": "2023-11-29",
}' http://example.com/posters/2
```

And as a response you get status 204.

## Delete a poster via the poster id - parent and admin

```
curl -X DELETE http://example.com/posters/1
```

And as a response you get status 204.

# Endpoints for babysitter specifics

## Get all babysitters - parent and admin

```
curl -X GET http://example.com/babysitters
```

You get a list of all of the babysitters in the following format :

```json
{
    "1": {
        "id": 1,
        "username": "",
        "email": "",
        "firstName": "",
        "lastName": "",
        "profileImage": "",
        "phoneNumber": "",
        "address": "",
        "role": "babysitter",
        "age": ,
        "gender": ,
        "points": 90,
        "available": false
    },
    "2": {
        "id": 2,
        "username": "",
        "email": "",
        "firstName": "",
        "lastName": "",
        "profileImage": "",
        "phoneNumber": "",
        "address": "",
        "role": "babysitter",
        "age": ,
        "gender": ,
        "points": 40,
        "available": true
    },
}
```

## Update babysitter points - parent only

```
curl -X PATCH -H "Content-Type: application/json" -d '{
    "atrribute_name": ""
}' http://example.com/babysitters/1
```

As a return you get response 204.

# Endpoints for parents specifics

## Get all parents - admin only

```
curl -X GET http://example.com/parents
```

You get a list of all of the parents in the following format :

```json
{
    "1": {
        "id": 1,
        "username": "",
        "email": "",
        "firstName": "",
        "lastName": "",
        "profileImage": "",
        "phoneNumber": "",
        "address": "",
        "role": "parent",
        "age": ,
    },
    "2": {
        "id": 2,
        "username": "",
        "email": "",
        "firstName": "",
        "lastName": "",
        "profileImage": "",
        "phoneNumber": "",
        "address": "",
        "role": "parent",
        "age": ,
    },
}
```

# Endpoints for jobApplications

## Create a jobApplication - babysitter only

```
curl -X POST -H "Content-Type: application/json" -d '{
    "description": "",
    "status" "Pending",
    "babysitterId" 1,
    "posterId" 1,
    "parentId" 2,
}' http://example.com/jobApplications/
```

You get 204 as response.

## Get babysitter jobApplications via the babysitter Id - babysitter only

```
curl -X GET http://example.com/jobApplications/babysitter/1
```

You get the jobApplications in the following json

```json
{
  "1": {
    "id": 1,
    "description": "",
    "status": "Pending",
    "babysitterId": 1,
    "posterId": 1,
    "parentId": 2
  },
  "2": {
    "id": 2,
    "description": "",
    "status": "Approved",
    "babysitterId": 1,
    "posterId": 2,
    "parentId": 2
  }
}
```

## Get parent jobApplications via the parent Id - parent only

```
curl -X GET http://example.com/jobApplications/parent/1
```

You get the jobApplications in the following json

```json
{
  "1": {
    "id": 1,
    "description": "",
    "status": "Pending",
    "babysitterId": 1,
    "posterId": 1,
    "parentId": 2
  },
  "2": {
    "id": 2,
    "description": "",
    "status": "Approved",
    "babysitterId": 1,
    "posterId": 2,
    "parentId": 2
  }
}
```

## Delete a jobApplication - parent only

If you need to delete a jobApplication just :

```
curl -X DELETE http://example.com/jobApplications/1
```

As a return you get response 204.

## Change the status of a jobApplication - parent only

```
curl -X PATCH -H "Content-Type: application/json" -d '{
    "atrribute_name": ""
}' http://example.com/jobApplications/1
```

As a return you get response 204.

## CI/CD

The pipeline is configured for [GitLab](https://about.gitlab.com/) using GitLab veriables for security.
The pipeline has 4 stages:

- build
- test
- build_image
- sonar

The build builds the application and if it does not fail goes to the next stage - runs all tests. If it passes it builds a docker image from the Dockerfile and pushes it to Docker Hub. The last stage is the SonarQube code analysis which gives statistics for the code quality (found on http://localhost:9000).
