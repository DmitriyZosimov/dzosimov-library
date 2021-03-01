package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.BookDao;
import com.epam.brest.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.epam.brest.model.Genre;
import com.epam.brest.model.ReaderProxy;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class BookDaoSpringJdbc implements BookDao, InitializingBean {
    private static final String FIND_ALL = "select * from lib_book";
    private static final String FIND_BOOK_BY_ID =
            "select * from lib_book where book_id = :bookId";
    private static final String INSERT_BOOK =
            "insert into lib_book (authors, title, genre) VALUES (:authors, :title, :genre)";
    private static final String UPDATE_BOOK =
            "UPDATE lib_book SET authors=:authors, title=:title, genre=:genre WHERE book_id=:bookId";
    private static final String DELETE_BOOK =
            "delete from lib_book where book_id = :bookId";
    private static final String EXIST_BOOK =
            "select exists(select * from lib_book " +
                    "where book_id=:bookId and authors=:authors and title=:title and genre=:genre)";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL, new BookMapper());
    }

    @Override
    public Optional<Book> findBookById(Integer id) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("bookId", id);
        return Optional.ofNullable((Book) namedParameterJdbcTemplate.
                queryForObject(FIND_BOOK_BY_ID, sqlParameterSource, new BookMapper()));
    }

    @Override
    public Book save(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("authors", book.getAuthors());
        sqlParameterSource.addValue("title", book.getTitle());
        sqlParameterSource.addValue("genre", book.getGenre().ordinal());
        namedParameterJdbcTemplate.update(INSERT_BOOK, sqlParameterSource, keyHolder);
        book.setId(Objects.requireNonNull(keyHolder.getKey().intValue()));
        return book;
    }

    @Override
    public Integer update(Book book) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("bookId", book.getId());
        sqlParameterSource.addValue("authors", book.getAuthors());
        sqlParameterSource.addValue("title", book.getTitle());
        sqlParameterSource.addValue("genre", book.getGenre().ordinal());
        return namedParameterJdbcTemplate.update(UPDATE_BOOK, sqlParameterSource);
    }

    @Override
    public Integer delete(Book book) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("bookId", book.getId());
        return namedParameterJdbcTemplate.update(DELETE_BOOK, sqlParameterSource);
    }

    @Override
    public boolean exist(Book book) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("bookId", book.getId());
        sqlParameterSource.addValue("authors", book.getAuthors());
        sqlParameterSource.addValue("title", book.getTitle());
        sqlParameterSource.addValue("genre", book.getGenre().ordinal());
        return namedParameterJdbcTemplate.queryForObject(EXIST_BOOK, sqlParameterSource, Boolean.class);
    }


    @Override
    public void afterPropertiesSet() throws BeanCreationException {
        if(namedParameterJdbcTemplate == null){
            throw new BeanCreationException("Null JdbcTemplate on BookDaoSpringJdbc");
        }
    }

    private static final class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            Book book = new Book();
            book.setId(resultSet.getInt("book_id"));
            book.setAuthors(resultSet.getString("authors"));
            book.setTitle(resultSet.getString("title"));
            Genre genre = Genre.values()[resultSet.getInt("genre")];
            book.setGenre(genre);
            book.setReader(new ReaderProxy(resultSet.getInt("reader_id")));
            return book;
        }
    }
}
