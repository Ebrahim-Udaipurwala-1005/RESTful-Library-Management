# RESTful Library Management

Developed as a solution for the "RESTful Library Management" exercise in the **INHN0006 Introduction to Software Engineering** course, led by Prof. Dr. Stefan Wagner, this project serves as a modern library catalog system. It is designed to provide practical experience with building a complete client-server application.

The core of the project is a **RESTful API** that handles essential **CRUD (Create, Read, Update, Delete)** operations for a book collection, along with specific business-logic actions like checking books in and out. The server-side is built with **Spring Web**, efficiently handling incoming REST requests, while the client-side leverages **Spring WebFlux** to send these requests and manage the application's user interface. This layered architecture showcases a practical approach to building robust, modern software systems.

---

## Application Architecture

The application follows a **layered architecture** to separate concerns on both the client and server sides.

### Client
* **Presentation Layer** (View): The user interface.
* **Application Layer** (Controller): Manages application logic.
* **Network Layer** (WebClient): Handles sending HTTP requests to the server.

### Server
* **Network Layer** (Resource): The entry point for all incoming requests.
* **Business Layer** (Service): Contains the core business logic for managing books.

The client also uses the **Model-View-Controller (MVC)** pattern to structure its GUI components.

---

## API Endpoints and Functionality

The server exposes a set of REST endpoints on port `8080` for the `/books` resource.

### CRUD Operations
* **`POST /books`**: Creates a new book. A new book's status defaults to `AVAILABLE`.
* **`GET /books`**: Retrieves a list of all books. This endpoint supports filtering by author and genre simultaneously through optional query parameters.
* **`PUT /books/{bookId}`**: Updates a book's metadata (e.g., title, author).
* **`DELETE /books/{bookId}`**: Deletes a book from the system.

### Action-Based Operations
* **`POST /books/{bookId}/checkout`**: Changes a book's status from `AVAILABLE` to `CHECKED_OUT`.
* **`POST /books/{bookId}/return`**: Changes a book's status from `CHECKED_OUT` to `AVAILABLE`.

If an action cannot be performed (e.g., checking out a book that is already checked out), the server responds with an **HTTP 409 Conflict** status.

---

## How to Run the Application

To run the application locally, you must start both the server and the client.

1.  **Start the Server**: Run the `H10E01ServerApplication` class or use the Gradle task `bootRun`.
2.  **Start the Client**: After the server is running, run the `Starter` class or the Gradle task `run` to launch the client GUI.
