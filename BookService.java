package com.kiet.library.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kiet.library.model.Book;

@Service
public class BookService {

    private final List<Book> books = new ArrayList<>();

    public BookService() {
        books.add(new Book(201, "Effective Java",
                "Joshua Bloch", "Programming", 2018, true));

        books.add(new Book(202, "Clean Code",
                "Robert C. Martin", "Programming", 2008, true));

        books.add(new Book(203, "The Alchemist",
                "Paulo Coelho", "Fiction", 1988, false));

        books.add(new Book(204, "Wings of Fire",
                "A.P.J. Abdul Kalam", "Biography", 1999, true));

        books.add(new Book(205, "Introduction to Algorithms",
                "Thomas H. Cormen", "Education", 2009, true));
    }

    // Get all books
    public List<Book> getAllBooks() {
        return books;
    }

    // Get book by ID
    public Book getBookById(int bookId) {
        for (Book b : books) {
            if (b.getBookId() == bookId) {
                return b;
            }
        }
        return null;
    }

    // Add a book
    public String addBook(Book book) {
        books.add(book);
        return "Book added successfully";
    }

    // Update a book
    public String updateBook(int bookId, Book updatedBook) {
        for (Book b : books) {
            if (b.getBookId() == bookId) {
                b.setTitle(updatedBook.getTitle());
                b.setAuthor(updatedBook.getAuthor());
                b.setCategory(updatedBook.getCategory());
                b.setPublicationYear(
                        updatedBook.getPublicationYear());
                b.setAvailable(updatedBook.isAvailable());

                return "Book updated successfully";
            }
        }
        return "Book not found";
    }

    // Delete a book
    public String deleteBook(int bookId) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId() == bookId) {
                books.remove(i);
                return "Book deleted successfully";
            }
        }
        return "Book not found";
    }

    // Search books by category
    public List<Book> searchByCategory(String category) {
        List<Book> result = new ArrayList<>();

        for (Book b : books) {
            if (b.getCategory().equalsIgnoreCase(category)) {
                result.add(b);
            }
        }

        return result;
    }

    // Find books published from a specified year onward
    public List<Book> findPublishedFromYear(int fromYear) {
        List<Book> result = new ArrayList<>();

        for (Book b : books) {
            if (b.getPublicationYear() >= fromYear) {
                result.add(b);
            }
        }

        return result;
    }
}