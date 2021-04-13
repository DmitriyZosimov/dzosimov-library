# **Library test project**
This is a sample of a 'Library' web application.
## Requirements
 - JDK 11
 - Apache Maven
## Build application
```
mvn clean install
```
## Start applications
### Start Rest application
To start Rest server:
```
java -jar ./rest-app/target/rest-app-1.0-SNAPSHOT.jar
```
Server up on http://localhost:8060

### Start Web application
To start Web application:
```
java -jar ./web-app/target/web-app-1.0-SNAPSHOT.jar
```
Server up on http://localhost:8070

## Database
Also you can connect to the h2 database on http://localhost:8060/h2-console
using default username "sa" without password and url "jdbc:h2:mem:testdb".

## Existing library cards
Eight readers already exist in the database.
You can use their profiles. For this you should sign in by identification
from 1 to 8.
## Available REST endpoints
### Postman collection
You can use the Postman.
See [postman.md](postman.md)


### Version
```
curl --request GET 'http://localhost:8060/version'
```
### Books
#### Find all books
```
curl --request GET 'http://localhost:8060/book' | json_pp
```
#### Find a book by identification
```
curl --request GET 'http://localhost:8060/book/1' | json_pp
```

#### Save a new book
```
curl --request POST 'http://localhost:8060/book' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
	"authors": "postman",
	"title": "postman",
	"genre": "ART"
}'
```

#### Edit a book
```
curl --request PUT 'http://localhost:8060/book' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
	"id": 1,
    "authors": "postman",
    "title": "postman",
    "genre": "CHILDREN",
    "quantity": 1
}'
```
#### Delete a book by identification
```
curl --request DELETE 'http://localhost:8060/book/1'
```

#### Tie a reader and a book
```
curl --request GET 'http://localhost:8060/book/2/reader/3'
```

#### Delete a tie of a reader and a book
```
curl --request DELETE 'http://localhost:8060/book/2/reader/3'
```

#### Search books by parameters
```
curl --request POST 'http://localhost:8060/book/search' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "authors": "o",
    "title": "",
    "genre": "DEFAULT"
}'
```

### Readers
#### Check if the reader is removed
```
curl --request GET 'http://localhost:8060/login/1/removed'
```

#### Check to exist the reader by identification
```
curl --request GET 'http://localhost:8060/login/1'
```

#### Get profile of a reader by identification
```
curl --request GET 'http://localhost:8060/reader/1' | json_pp
```

#### Save a new reader
```
curl --request POST 'http://localhost:8060/reader' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName": "postman",
    "lastName": "postman",
    "patronymic": "CHILDREN"
}'
```

#### Edit a reader
```
curl --request PUT 'http://localhost:8060/reader' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "readerId": 1,
    "firstName": "test",
    "lastName": "test",
    "patronymic": "test",
    "dateOfRegistry": "2021-04-09"
}'
```
#### Delete a reader by identification
```
curl --request DELETE 'http://localhost:8060/reader/1'
```

#### Restore a reader by identification
```
curl --request PUT 'http://localhost:8060/reader/1'
```

#### Find all readers
```
curl --request GET 'http://localhost:8060/reader' | json_pp
```

#### Find readers by date
```
curl --request POST 'http://localhost:8060/readers/search' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
	"from": "2020-01-13",
	"to": "2021-01-01"
}'
```
