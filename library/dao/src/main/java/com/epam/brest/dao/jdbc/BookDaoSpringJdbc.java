package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.BookDao;
import com.epam.brest.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.epam.brest.model.Genre;
import com.epam.brest.model.IReader;
import com.epam.brest.model.ReaderProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class BookDaoSpringJdbc implements BookDao, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookDaoSpringJdbc.class);

    @Value("${book.jdbc.findAll}")
    private String findAllSql;
    @Value("${book.jdbc.findBookById}")
    private String findBookByIdSql;
    @Value("${book.jdbc.save}")
    private String saveSql;
    @Value("${book.jdbc.update}")
    private String updateSql;
    @Value("${book.jdbc.delete}")
    private String deleteSql;
    @Value("${book.jdbc.exist}")
    private String existSql;
    @Value("${book.jdbc.addReaderForBook}")
    private String addReaderForBookSql;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        LOGGER.debug("namedParameterJdbcTemplate was initialized by {}", namedParameterJdbcTemplate);
    }

    @Override
    public List<Book> findAll() {
        LOGGER.info("findAll() was started");
        return namedParameterJdbcTemplate.query(findAllSql, new BookMapper());
    }

    @Override
    public Optional<Book> findBookById(Integer id) {
        LOGGER.info("findBookById(id) was started");
        LOGGER.debug("id={}", id);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("bookId", id);
        Book book = namedParameterJdbcTemplate.
                queryForObject(findBookByIdSql, sqlParameterSource, new BookMapper());
        return Optional.ofNullable(book);
    }

    @Override
    public Book save(Book book) {
        LOGGER.info("save(book) was started");
        LOGGER.debug("book={}", book);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("authors", book.getAuthors());
        sqlParameterSource.addValue("title", book.getTitle());
        sqlParameterSource.addValue("genre", book.getGenre().ordinal());
        namedParameterJdbcTemplate.update(saveSql, sqlParameterSource, keyHolder);
        book.setId(Objects.requireNonNull(keyHolder.getKey().intValue()));
        return book;
    }

    @Override
    public Integer update(Book book) {
        LOGGER.info("update(book) was started");
        LOGGER.debug("book={}", book);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("bookId", book.getId());
        sqlParameterSource.addValue("authors", book.getAuthors());
        sqlParameterSource.addValue("title", book.getTitle());
        sqlParameterSource.addValue("genre", book.getGenre().ordinal());
        return namedParameterJdbcTemplate.update(updateSql, sqlParameterSource);
    }

    @Override
    public Integer delete(Book book) {
        LOGGER.info("delete(book) was started");
        LOGGER.debug("book={}", book);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("bookId", book.getId());
        return namedParameterJdbcTemplate.update(deleteSql, sqlParameterSource);
    }

    @Override
    public boolean exist(Book book) {
        LOGGER.info("exist(book) was started");
        LOGGER.debug("book={}", book);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("bookId", book.getId());
        sqlParameterSource.addValue("authors", book.getAuthors());
        sqlParameterSource.addValue("title", book.getTitle());
        sqlParameterSource.addValue("genre", book.getGenre().ordinal());
        return namedParameterJdbcTemplate.queryForObject(existSql, sqlParameterSource, Boolean.class);
    }

    @Override
    public Integer addReaderForBook(Book book, IReader reader) {
        LOGGER.info("addReaderForBook(book, reader) was started");
        LOGGER.debug("book={}, reader={}", book, reader);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("readerId", reader.getReaderId());
        sqlParameterSource.addValue("bookId", book.getId());
        return  namedParameterJdbcTemplate.update(addReaderForBookSql, sqlParameterSource.getValues());
    }

    @Override
    public Integer removeReaderFromBook(Book book) {
        LOGGER.info("removeReaderFromBook(book) was started");
        LOGGER.debug("book={}", book);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("readerId", null);
        sqlParameterSource.addValue("bookId", book.getId());
        return  namedParameterJdbcTemplate.update(addReaderForBookSql, sqlParameterSource.getValues());
    }


    @Override
    public void afterPropertiesSet() throws BeanCreationException {
        if(namedParameterJdbcTemplate == null){
            LOGGER.error("namedParametrJdbcTemplate is null");
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
            int readerId = resultSet.getInt("reader_id");
            if(readerId > 0){
                book.setReader(new ReaderProxy(readerId));
            }
            LOGGER.info("BookMapper.class mapRow(resultSet, i) was started");
            LOGGER.debug("book={}", book);
            return book;
        }
    }
}
