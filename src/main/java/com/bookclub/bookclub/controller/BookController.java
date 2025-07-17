package com.bookclub.bookclub.controller;

import com.bookclub.bookclub.model.Book;
import com.bookclub.bookclub.model.Comment;
import com.bookclub.bookclub.repository.BookRepository;
import com.bookclub.bookclub.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class BookController {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public BookController(BookRepository bookRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
    }


    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/{id}")
    public String viewBook(@PathVariable("id") Long id, Model model) {
        Book book = bookRepository.findById(id);
        if (book == null) return "redirect:/books";
        List<Comment> comments = commentRepository.findByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        model.addAttribute("newComment", new Comment());
        return "book-details";
    }

    @PostMapping("/books/{id}/comments")
    public String postComment(@PathVariable("id") Long id, @ModelAttribute Comment newComment) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        newComment.setBookId(id);
        newComment.setTimestamp(timestamp);
        commentRepository.save(newComment);
        return "redirect:/books/" + id;
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book) {
        bookRepository.save(book);  // Save returns ID if you're using auto-generated PK
        Long newBookId = book.getId(); // After save, ID is populated
        return "redirect:/books/" + newBookId;
    }

    @GetMapping("/books/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

}
