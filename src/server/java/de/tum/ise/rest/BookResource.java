package de.tum.ise.rest;

import de.tum.ise.model.Book;
import de.tum.ise.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        return ResponseEntity.ok(savedBook);
    }
    // - PUT /books/{bookId}: Update a book.
    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable UUID bookId, @RequestBody Book book) {
        book.setId(bookId);
        Book updatedBook = bookService.saveBook(book);
        return ResponseEntity.ok(updatedBook);
    }
    // - DELETE /books/{bookId}: Delete a book.
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }
    // - GET /books: Get all books, with optional filtering by author and genre.
//    @GetMapping
//    public ResponseEntity<List<Book>> getAllBooks() {}
    // - POST /books/{bookId}/checkout: Checks out a book. Return 409 (Conflict) if not possible.
    @PostMapping("/{bookId}/checkout")
    public ResponseEntity<Book> checkoutBook(@PathVariable UUID bookId) {
        return bookService.checkOutBook(bookId).map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }
    // - POST /books/{bookId}/return: Returns a book. Return 409 (Conflict) if not possible.
    @PostMapping("/{bookId}/return")
    public ResponseEntity<Book> returnBook(@PathVariable UUID bookId) {
        return bookService.returnBook(bookId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

}