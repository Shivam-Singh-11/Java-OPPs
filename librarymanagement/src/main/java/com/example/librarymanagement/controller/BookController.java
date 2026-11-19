package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.Book;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    List<Book> books = new ArrayList<>();

    // (b) Add a new book
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {

        for (Book b : books) {
            if (b.getId().equals(book.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Book already exists");
            }
        }

        books.add(book);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(book);
    }

    // (c) Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }

    // (c) Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {

        for (Book b : books) {
            if (b.getId().equals(id)) {
                return ResponseEntity.ok(b);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found");
    }

    // (d) Update book
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable Integer id,
            @RequestBody Book updatedBook) {

        for (Book b : books) {

            if (b.getId().equals(id)) {

                b.setTitle(updatedBook.getTitle());
                b.setAuthor(updatedBook.getAuthor());
                b.setCategory(updatedBook.getCategory());
                b.setAvailableCopies(updatedBook.getAvailableCopies());

                return ResponseEntity.ok(b);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found");
    }

    // (e) Delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {

        boolean removed = books.removeIf(
                b -> b.getId().equals(id)
        );

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found");
    }
}