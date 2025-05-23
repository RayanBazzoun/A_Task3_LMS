# Library Management System API Documentation

## Overview
This document provides a detailed explanation of the database design, API endpoints, and validation rules for the Library Management System project.

---

## Database Design

### 1. Author
- **Table:** `author`
- **Fields:**
    - `id` (UUID, Primary Key)
    - `name` (String, Not Null)
    - `biography` (Text, Optional)

### 2. Book
- **Table:** `book`
- **Fields:**
    - `id` (UUID, Primary Key)
    - `isbn` (String, Unique, Not Null)
    - `title` (String, Not Null)
    - `category` (String, Not Null – Enum in code, e.g., FICTION, HISTORY, etc.)
    - `availability` (Boolean, Not Null, Default: true)
- **Relationships:**
    - Many-to-Many with Author via `book_author` table

### 3. Book_Author
- **Table:** `book_author`
- **Fields:**
    - `book_id` (UUID, Foreign Key → Book.id)
    - `author_id` (UUID, Foreign Key → Author.id)
- **Relationships:**
    - Composite Primary Key: (book_id, author_id)

### 4. Borrower
- **Table:** `borrower`
- **Fields:**
    - `id` (UUID, Primary Key)
    - `name` (String, Not Null)
    - `email` (String, Optional)
    - `phone_number` (String, Optional)

### 5. BorrowingTransaction
- **Table:** `borrowing_transaction`
- **Fields:**
    - `id` (UUID, Primary Key)
    - `book_id` (UUID, Foreign Key → book.id)
    - `borrower_id` (UUID, Foreign Key → borrower.id)
    - `borrow_date` (Date, Not Null)
    - `return_date` (Date, Optional)
    - `status` (String, Not Null – Enum in code: BORROWED, RETURNED)

---

## API Endpoints

### Book APIs  
**Base Path:** `/books`

| Method | Endpoint         | Description                      | Request Body Attributes                                                                          |
|--------|------------------|----------------------------------|--------------------------------------------------------------------------------------------------|
| POST   | `/books`         | Add a new book                   | `title` (String, required), `isbn` (String, required), `category` (String, required), `authorIds` (List of UUIDs, required) |
| GET    | `/books/{id}`    | Get details of a specific book   | —                                                                                                |
| PUT    | `/books/{id}`    | Update a book                    | `title` (String), `isbn` (String), `category` (String), `authorIds` (List of UUIDs)             |
| DELETE | `/books/{id}`    | Delete a book                    | —                                                                                                |
| GET    | `/books`         | Search books                     | Query params: `title` (String), `category` (String), `author` (String or UUID)                   |

### Author APIs  
**Base Path:** `/authors`

| Method | Endpoint              | Description                       | Request Body Attributes        |
|--------|-----------------------|-----------------------------------|-------------------------------|
| POST   | `/authors`            | Add a new author                  | `name` (String, required), `biography` (String, optional) |
| GET    | `/authors/{id}`       | Get details of a specific author  | —                             |
| PUT    | `/authors/{id}`       | Update an author                  | `name` (String), `biography` (String) |
| DELETE | `/authors/{id}`       | Delete an author                  | —                             |
| GET    | `/authors/{id}/books` | Get all books by an author        | —                             |

### Borrower APIs  
**Base Path:** `/borrowers`

| Method | Endpoint           | Description                        | Request Body Attributes                       |
|--------|--------------------|------------------------------------|-----------------------------------------------|
| POST   | `/borrowers`       | Add a new borrower                 | `name` (String, required), `email` (String, optional), `phone_number` (String, optional) |
| GET    | `/borrowers/{id}`  | Get details of a specific borrower | —                                            |
| PUT    | `/borrowers/{id}`  | Update a borrower                  | `name` (String), `email` (String), `phone_number` (String)  |
| DELETE | `/borrowers/{id}`  | Delete a borrower                  | —                                            |

### Borrowing Transaction APIs  
**Base Path:** `/transactions`

| Method | Endpoint                                 | Description         | Request Body Attributes                                                |
|--------|------------------------------------------|---------------------|------------------------------------------------------------------------|
| POST   | `/transactions/borrow`                   | Borrow a book       | `bookId` (UUID, required), `borrowerId` (UUID, required)               |
| POST   | `/transactions/return/{transactionId}`   | Return a book       | —                                                                      |

---

## Validation Rules

### Book
- ISBN must be unique.
- Category must be one of the allowed enums (e.g., FICTION, NON_FICTION, SCIENCE, HISTORY, etc.).
- At least one author must be provided.
- Title must not be empty.
- Availability is managed by the system (cannot be set to unavailable if book is currently borrowed).

### Author
- Name must not be empty.

### Borrower
- Name must not be empty.
- Email and phone number are optional but, if provided, must be valid format.

### Borrowing Transaction
- Book must exist and be available for borrowing.
- Borrower must exist.
- A book cannot be borrowed if already borrowed and not yet returned.
- Returning a book updates its availability and sets the transaction status to RETURNED.
- Only BORROWED and RETURNED are allowed as status.

---

## Error Handling
- All validation and business logic errors return appropriate HTTP status codes and error messages.
- Example: Trying to borrow an unavailable book returns 400 Bad Request with a descriptive error.

---

## Logging
- All API operations are logged for traceability and debugging.

---
