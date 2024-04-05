# Library Management System 


## Project description
The Library Management System is a platform for managing library resources and services.
When the users are too busy, they do not have to come to the library and search for their favourite book.
They can access the library's resources online, search for books, reserve books, borrow and return books.
The customers can search and list the books online even without creating an account.
When they create an account they become the members and may reserve, borrow and return books.
The system also provide a librarian role.
The librarians are primarily responsible for managing the library resources, adding new book and updating book information.
Secondarily, they can manage user accounts and help them with searching, reserving, borrowing or returning the books.
Each borrow has a price and a limit in days for returning.
If this limit is not kept, there is a fine set during the borrowing process for each delayed day.

## Microservices
### Account

#### Overview

The Account microservice allows users to create accounts inside library management system.
It's accessible at http://localhost:8082. Also, via GUI on http://localhost:8082/swagger-ui/index.html#/.

#### Endpoints

- **GET /api/users:** Retrieve all users.
- **POST /api/users:** Create a new user.
- **GET /api/users/{id}:** Retrieve a user by its ID.
- **PATCH /api/users/{id}:** Update an existing user.
- **DELETE /api/users/{id}:** Delete a user by its ID.
- **GET /api/users/adults:** Retrieve all adults.
### Book
tbd
### Catalogue
tbd
### Fine
tbd
### Rental
tbd

## Use Case Diagram

![](./puml/useCaseDiagram.png "Use case diagram of library management system")

## Class Diagram for the DTOs

![](./puml/classDiagram.png "Class diagram defining DTOs of library management system")





