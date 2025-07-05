package de.tum.ise.rest;

import de.tum.ise.model.Book;
import de.tum.ise.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("books")
public class BookResource {

    private final BookService bookService;

    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }

    // TODO Part 1 & 3: Implement ALL the required endpoints here.
    // - POST /books: Create a book.
    // - PUT /books/{bookId}: Update a book.
    // - DELETE /books/{bookId}: Delete a book.
    // - GET /books: Get all books, with optional filtering by author and genre.
    // - POST /books/{bookId}/checkout: Checks out a book. Return 409 (Conflict) if not possible.
    // - POST /books/{bookId}/return: Returns a book. Return 409 (Conflict) if not possible.

}