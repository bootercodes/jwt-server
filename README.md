# Sample JWT Auth Server

## Requirement:
- JAVA 21
- Maven

## Getting started

- After cloning, run following command in the root of the folder to build project
> mvn clean install

- Run following command to start the server
> java -jar .\target\jwt-server-0.0.1-SNAPSHOT.jar

## List of APIs server exposes

The 3 sample APIs are used to:
1. Generate the JWT token
2. Then either call the 
   - Admin API (JWT must have Role=[ADMIN])
   - Or
   - User API (JWT must have ROLE=[ADMIN, USER])

### Auth API

> Note - This API returns JWT token

> curl --location 'localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data '{ "username": "Manager", "password": "password" }'

### Admin API

> Note - Role must be 'ADMIN' for the user accessing this resource

> curl --location 'localhost:8080/admin' \
--header 'Authorization: Bearer <jwt-token-here-generated-using-auth-api>'


### User API

> Note - Role must be 'USER' for the user accessing this resource


> curl --location 'localhost:8080/api/user' \
--header 'Authorization: Bearer <jwt-token-here-generated-using-auth-api>'


## User Store

> Users are hardcoded in the CustomUserDetailsService class. Class loads 2 users
>> [ {user:Employee, role:USER}, {user:Manager, role:ADMIN} ]
