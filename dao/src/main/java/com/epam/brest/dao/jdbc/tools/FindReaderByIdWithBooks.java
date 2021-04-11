package com.epam.brest.dao.jdbc.tools;

import com.epam.brest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FindReaderByIdWithBooks implements ResultSetExtractor<Reader> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindReaderByIdWithBooks.class);

    @Override
    public Reader extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        LOGGER.info("method extractData(...) started");
        Reader reader = null;
        while(resultSet.next()){
            Integer id = resultSet.getInt("reader_id");
            if(reader == null){
                reader = new Reader();
                reader.setReaderId(resultSet.getInt("reader_id"));
                reader.setFirstName(resultSet.getString("first_name"));
                reader.setLastName(resultSet.getString("last_name"));
                reader.setPatronymic(resultSet.getString("patronymic"));
                reader.setDateOfRegistry(resultSet.getDate("date_of_registry").toLocalDate());
                reader.setActive(resultSet.getBoolean("active"));
                LOGGER.debug("extractData: reader={}",reader);
                reader.setBooks(new ArrayList<>());
            }
            Integer bookId = resultSet.getInt("book_id");
            if(bookId > 0){
                Book book = new Book();
                book.setReaderId(reader.getReaderId());
                book.setId(bookId);
                book.setGenre(Genre.values()[resultSet.getInt("genre")]);
                book.setTitle(resultSet.getString("title"));
                book.setAuthors(resultSet.getString("authors"));
                reader.getBooks().add(book);
                LOGGER.debug("extractData: BookDto={}",book);
            }
        }
        return reader;
    }
}
