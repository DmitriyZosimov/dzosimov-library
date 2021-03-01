package com.epam.brest.dao.jdbc.readerdaosupport;

import com.epam.brest.model.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FindReaderByIdWithBooks implements ResultSetExtractor<IReader> {

    private static final String FIND_READER_BY_ID_WITH_BOOKS =
            "SELECT r.reader_id, first_name, last_name, patronymic, date_of_registry, active, " +
                    "book_id, authors, title, genre" +
                    " FROM lib_reader r LEFT JOIN lib_book b " +
                    "ON r.reader_id=b.reader_id " +
                    "WHERE r.reader_id=:readerId";

    public static String getFindReaderByIdWithBooks() {
        return FIND_READER_BY_ID_WITH_BOOKS;
    }

    @Override
    public IReader extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        IReader reader = null;
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
                reader.setBooks(new ArrayList<>());
            }
            Integer bookId = resultSet.getInt("book_id");
            if(bookId > 0){
                Book book = new Book();
                book.setReader(new ReaderProxy(reader.getReaderId()));
                book.setId(bookId);
                book.setGenre(Genre.values()[resultSet.getInt("genre")]);
                book.setTitle(resultSet.getString("title"));
                book.setAuthors(resultSet.getString("authors"));
                reader.getBooks().add(book);
            }
        }
        return reader;
    }
}
