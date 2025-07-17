package com.bookclub.bookclub.repository;

import com.bookclub.bookclub.model.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {
    private final JdbcTemplate jdbc;

    public CommentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Comment> rowMapper = (rs, rowNum) -> {
        Comment c = new Comment();
        c.setId(rs.getLong("id"));
        c.setBookId(rs.getLong("book_id"));
        c.setUsername(rs.getString("username"));
        c.setContent(rs.getString("content"));
        c.setTimestamp(rs.getString("timestamp"));
        return c;
    };

    public List<Comment> findByBookId(Long id) {
        return jdbc.query("SELECT * FROM comments WHERE book_id = ?", rowMapper, id);
    }

    public void save(Comment comment) {
        jdbc.update("INSERT INTO comments (book_id, username, content, timestamp) VALUES (?, ?, ?, datetime('now'))",
            comment.getBookId(), comment.getUsername(), comment.getContent());
    }
}