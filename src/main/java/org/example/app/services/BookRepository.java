package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Book> retreiveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowsNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
        logger.info("store new book: " + book);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());


        jdbcTemplate.update("INSERT INTO books(author, title, size) VALUES (:author, :title, :size)", parameterSource);
        if (book.getAuthor().length() > 0 || book.getTitle().length() > 0 || book.getSize() != null) {
            repo.add(book);
        }
        logger.info("store new book: no data to store");
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        logger.info("remove book by id " + bookIdToRemove);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        jdbcTemplate.update("DELETE FROM books WHERE id =:id", parameterSource);

        logger.info("book removed");
        return true;
    }

    @Override
    public List<Book> retreive(String field, String regex) {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books WHERE " + field + " LIKE'" + regex + "'", (ResultSet rs, int rowsNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });

        return new ArrayList<>(books);
    }

    @Override
    public List<String> getBooksColumns() {
        return jdbcTemplate.query("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS  where TABLE_NAME='BOOKS'", (ResultSet rs, int rowsNum) -> {
            return rs.getString("COLUMN_NAME").toLowerCase();
        });
    }
}
