package de.tum.ise.controller;

import de.tum.ise.model.Book;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BookController {

    private final List<Book> books;

    // TODO Part 2: Add all required attributes and implement the constructor
    public BookController() {
        this.books = new ArrayList<>();
    }

    // Helper method
    private void updateLocalBook(Book updatedBook, Consumer<List<Book>> booksConsumer) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(updatedBook.getId())) {
                books.set(i, updatedBook);
                break;
            }
        }
        booksConsumer.accept(new ArrayList<>(books));
    }

    public void addBook(Book book, Consumer<List<Book>> booksConsumer) {
        // TODO Part 2: Make an HTTP POST request to create a book.
    }

    public void updateBook(Book book, Consumer<List<Book>> booksConsumer) {
        // TODO Part 2: Make an HTTP PUT request to update a book.
    }

    public void deleteBook(Book book, Consumer<List<Book>> booksConsumer) {
        // TODO Part 2: Make an HTTP DELETE request to delete a book.
    }

    public void getAllBooks(String author, Book.Genre genre, Consumer<List<Book>> booksConsumer) {
        // TODO Part 2 and 3: Make an HTTP GET request to fetch books.
        // The URI should include 'author' and 'genre' as query parameters if they are not null/blank.
    }

    public void checkoutBook(Book book, Consumer<List<Book>> booksConsumer) {
        // TODO Part 2: Make an HTTP POST request to the checkout endpoint.
        // On success, the server returns the updated book. Use the `updateLocalBook` helper.
    }

    public void returnBook(Book book, Consumer<List<Book>> booksConsumer) {
        // TODO Part 2: Make an HTTP POST request to the return endpoint.
        // On success, the server returns the updated book. Use the `updateLocalBook` helper.
    }
}