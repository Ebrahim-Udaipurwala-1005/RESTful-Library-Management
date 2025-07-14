package de.tum.ise.service;

import de.tum.ise.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final List<Book> books;

    public BookService() {
        this.books = new ArrayList<>();
    }

    // Helper method
    public Optional<Book> findById(UUID id) {
        if (id == null) return Optional.empty();
        return books.stream()
                .filter(existingBook -> existingBook.getId().equals(id))
                .findFirst();
    }

    public Book saveBook(Book book) {
        var optionalBook = findById(book.getId());

        if (optionalBook.isEmpty()) {
            // Logic for creating a new book
            book.setId(UUID.randomUUID());
            // TODO Part 1: When a new book is created, what should its default status be?
            book.setStatus(Book.Status.AVAILABLE);
            // Set the book's status to AVAILABLE.
            books.add(book);
            return book;
        } else {
            // Logic for updating an existing book
            var existingBook = optionalBook.get();
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setIsbn(book.getIsbn());
            existingBook.setPublishedYear(book.getPublishedYear());
            existingBook.setGenre(book.getGenre());
            // Note: Status is NOT updated here. It's managed by checkout/return.
            return existingBook;
        }
    }

    public void deleteBook(UUID bookId) {
        this.books.removeIf(book -> book.getId().equals(bookId));
    }

    public List<Book> getAllBooks(String author, Book.Genre genre) {
        var bookStream = this.books.stream();

        // TODO Part 3: Implement the filtering logic.
        // If the author parameter is not null and not blank, filter the stream
        // for books where the author's name contains the search string (case-insensitive).
        // If the genre parameter is not null, also filter the stream by the given genre.

        return bookStream
                .filter(book -> (author == null || author.isBlank() || book.getAuthor().toLowerCase().contains(author.toLowerCase())))
                .filter(book -> (genre == null || book.getGenre().equals(genre)))
                .collect(Collectors.toList());
    }

    public Optional<Book> checkOutBook(UUID bookId) {
        // TODO Part 1: Implement the checkout logic.
        // 1. Find the book by its ID.
        // 2. If the book exists and its status is AVAILABLE, change its status to CHECKED_OUT.
        // 3. Return an Optional containing the updated book.
        // 4. If the book doesn't exist or is not AVAILABLE, return an empty Optional.
        return books.stream()
                .filter(b -> b.getId().equals(bookId))
                .filter(b -> b.getStatus() == Book.Status.AVAILABLE)
                .findFirst()
                .map(b -> {
                    b.setStatus(Book.Status.CHECKED_OUT);
                    return b;
                });
    }

    public Optional<Book> returnBook(UUID bookId) {
        // TODO Part 1: Implement the return logic.
        // 1. Find the book by its ID.
        // 2. If the book exists and its status is CHECKED_OUT, change its status to AVAILABLE.
        // 3. Return an Optional containing the updated book.
        // 4. If the book doesn't exist or is not CHECKED_OUT, return an empty Optional.
        return books.stream()
                .filter(b -> b.getId().equals(bookId))
                .filter(b -> b.getStatus() == Book.Status.CHECKED_OUT)
                .findFirst()
                .map(b -> {
                    b.setStatus(Book.Status.AVAILABLE);
                    return b;
                });
    }
}