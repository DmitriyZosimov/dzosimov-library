[![Java CI with Maven](https://github.com/Brest-Java-Course-2021/dzosimov/actions/workflows/maven.yml/badge.svg)](https://github.com/Brest-Java-Course-2021/dzosimov/actions/workflows/maven.yml)
#**Library test project**
This is a sample of 'Library' web application.
##Requirements
 - JDK 11
 - Apache Maven
##Build application:
```
mvn clean install
```
##Start applications
###Start Rest application
To start Rest server:
```
java -jar ./rest-app/target/rest-app-1.0-SNAPSHOT.jar
```
###Start Web application
To start Web application:
```
java -jar ./web-app/target/web-app-1.0-SNAPSHOT.jar
```
##Available REST endpoints
###Postman collection
You can use the Postman.
See [postman.md](postman.md)


###version
```
curl --request GET 'http://localhost:8060/version'
```
###books
####Find all books
```
curl --request GET 'http://localhost:8060/book' | json_pp
```
####Find a book by identification
```
curl --request GET 'http://localhost:8060/book/1' | json_pp
```

####Save a new book
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

####Edit the book
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
####Delete the book by identification
```
curl --request DELETE 'http://localhost:8060/book/1'
```

####Tie the reader and the book
```
curl --request GET 'http://localhost:8060/book/2/reader/3'
```

####Delete the tie of the reader and the book
```
curl --request DELETE 'http://localhost:8060/book/2/reader/3'
```

####Search books by parameters
```
curl --request POST 'http://localhost:8060/book/search' \
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
####Check if the reader is removed
```
curl --request GET 'http://localhost:8060/login/1/removed'
```

####Check to exist the reader by identification
```
curl --request GET 'http://localhost:8060/login/1'
```

####Get profile of the reader by identification
```
curl --request GET 'http://localhost:8060/reader/1' | json_pp
```

####Save a new reader
```
curl --request POST 'http://localhost:8060/book/search' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName": "postman",
    "lastName": "postman",
    "patronymic": "CHILDREN"
}'
```

####Edit the reader
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
####Delete the reader by identification
```
curl --request DELETE 'http://localhost:8060/reader/1'
```

####Restore the reader by identification
```
curl --request PUT 'http://localhost:8060/reader/1'
```

####Find all readers
```
curl --request GET 'http://localhost:8060/reader' | json_pp
```

####Find the readers by date
```
curl --request POST 'http://localhost:8060/readers/search' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
	"from": "2020-01-13",
	"to": "2021-01-01"
}'
```