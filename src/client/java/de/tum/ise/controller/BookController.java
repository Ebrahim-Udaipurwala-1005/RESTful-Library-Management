package de.tum.ise.controller;

import de.tum.ise.model.Book;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BookController {

    private final List<Book> books;
    private final WebClient webClient;

    // TODO Part 2: Add all required attributes and implement the constructor
    public BookController() {
        this.books = new ArrayList<>();
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
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
        webClient.post()
                .uri("")
                .bodyValue(book).retrieve()
                .bodyToMono(Book.class).doOnNext(newBook -> {
                    books.add(newBook);
                    booksConsumer.accept(new ArrayList<>(books));
                })
                .subscribe();
    }

    public void updateBook(Book book, Consumer<List<Book>> booksConsumer) {
        // TODO Part 2: Make an HTTP PUT request to update a book.
        if (book == null || book.getId() == null) {  // Guard clause
            throw new IllegalArgumentException("Book ID cannot be null for update");
        }
        else {
            webClient.put()
                    .uri("/{id}", book.getId())
                    .bodyValue(book)
                    .retrieve()
                    .bodyToMono(Book.class)
                    .doOnNext(newBook -> {
                        updateLocalBook(newBook, booksConsumer);
                    })
                    .subscribe();
        }
    }

    public void deleteBook(Book book, Consumer<List<Book>> booksConsumer) {
        // TODO Part 2: Make an HTTP DELETE request to delete a book.
        if (book == null || book.getId() == null) {  // Guard clause
            throw new IllegalArgumentException("Book ID cannot be null for deletion");
        }
        else {
            webClient.delete()
                    .uri("/{id}", book.getId())
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(response -> {
                        books.removeIf(b -> b.getId().equals(book.getId()));
                        booksConsumer.accept(new ArrayList<>(books));
                    })
                    .subscribe();

        }
    }

    public void getAllBooks(String author, Book.Genre genre, Consumer<List<Book>> booksConsumer) {
        // TODO Part 2 and 3: Make an HTTP GET request to fetch books.
        // The URI should include 'author' and 'genre' as query parameters if they are not null/blank.
        webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("/books");
                    if (author != null && !author.isEmpty()) {
                        builder.queryParam("author", author);
                    }
                    if (genre != null) {
                        builder.queryParam("genre", genre.name());
                    }
                    return builder.build();
                })
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<Book>>() {})
                .subscribe(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        books.clear();
                        books.addAll(response.getBody());
                        booksConsumer.accept(new ArrayList<>(books));
                    }
                });
    }

    @PostMapping("/books/{id}/checkout")
    public void checkoutBook(Book book, Consumer<List<Book>> booksConsumer) {
        webClient.post()
                .uri("/books/{id}/checkout", book.getId())
                .retrieve()
                .toEntity(Book.class)
                .subscribe(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        updateLocalBook(response.getBody(), booksConsumer);
                    }
                    // Handle errors if needed
                });
    }

    public void returnBook(Book book, Consumer<List<Book>> booksConsumer) {
        webClient.post()
                .uri("/books/{id}/return", book.getId())
                .retrieve()
                .toEntity(Book.class)
                .subscribe(response -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        updateLocalBook(response.getBody(), booksConsumer);
                    }
                    // Handle errors if needed
                });
    }
}