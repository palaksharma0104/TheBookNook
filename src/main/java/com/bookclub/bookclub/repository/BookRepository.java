package com.bookclub.bookclub.repository;

import com.bookclub.bookclub.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {
    private final JdbcTemplate jdbc;

    public BookRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Book> rowMapper = (rs, rowNum) -> {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setGenre(rs.getString("genre"));
        book.setDescription(rs.getString("description"));
        return book;
    };

    public List<Book> findAll() {
        return jdbc.query("SELECT * FROM books", rowMapper);
    }

    public Book findById(Long id) {
        return jdbc.queryForObject("SELECT * FROM books WHERE id = ?", rowMapper, id);
    }

    public void save(Book book) {
        jdbc.update("INSERT INTO books (title, author, genre, description) VALUES (?, ?, ?, ?)",
            book.getTitle(), book.getAuthor(), book.getGenre(), book.getDescription());
        
        Long newId = jdbc.queryForObject("SELECT last_insert_rowid()", Long.class);
        book.setId(newId);
        }
    
}


