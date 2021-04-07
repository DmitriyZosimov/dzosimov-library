package com.epam.brest.dao.jdbc.tools;

import com.epam.brest.model.Book;
import com.epam.brest.model.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BookMapper implements RowMapper<Book> {

    private final Logger LOGGER = LoggerFactory.getLogger(BookMapper.class);

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt("book_id"));
        book.setAuthors(resultSet.getString("authors"));
        book.setTitle(resultSet.getString("title"));
        Genre genre = Genre.values()[resultSet.getInt("genre")];
        book.setGenre(genre);
        LOGGER.info("BookMapper.class mapRow(resultSet, i) was started");
        LOGGER.debug("Book={}", book);
        return book;
    }
}
