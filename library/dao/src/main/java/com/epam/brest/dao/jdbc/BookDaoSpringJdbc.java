package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.BookDao;
import com.epam.brest.dao.jdbc.tools.BookMapper;
import com.epam.brest.dao.jdbc.tools.CatalogOfBooksMapper;
import com.epam.brest.model.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.epam.brest.model.Genre;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
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
    @Value("${book.jdbc.removeReaderForBooks}")
    private String removeReaderForBooksSql;
    @Value("${book.jdbc.searchBooks}")
    private String searchBooksSql;
    @Value("${book.jdbc.searchBooksWithGenre}")
    private String searchBooksWithGenreSql;
    @Value("${book.jdbc.removeReaderFromBook}")
    private String removeReaderFromBookSql;


    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private BookMapper bookMapper;
    private CatalogOfBooksMapper catalogOfBooksMapper;

    public BookDaoSpringJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                             BookMapper bookMapper, CatalogOfBooksMapper catalogOfBooksMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.bookMapper = bookMapper;
        this.catalogOfBooksMapper = catalogOfBooksMapper;
    }

    @Override
    public List<BookSample> findAll() {
        LOGGER.info("findAll() was started");
        return namedParameterJdbcTemplate.query(findAllSql, catalogOfBooksMapper);
    }

    @Override
    public Optional<Book> findBookById(Integer id) {
        LOGGER.info("findBookById(id) was started");
        LOGGER.debug("id={}", id);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("bookId", id);
        Book book = namedParameterJdbcTemplate.
                queryForObject(findBookByIdSql, sqlParameterSource, bookMapper);
        return Optional.ofNullable(book);
    }

    @Override
    public Book save(Book book) {
        LOGGER.info("save(BookDto) was started");
        LOGGER.debug("Book={}", book);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("authors", book.getAuthors());
        sqlParameterSource.addValue("title", book.getTitle());
        sqlParameterSource.addValue("genre", book.getGenre().ordinal());
        namedParameterJdbcTemplate.update(saveSql, sqlParameterSource, keyHolder);
        book.setId(keyHolder.getKey().intValue());
        return book;
    }

    @Override
    public Integer update(BookSample bookSample) {
        LOGGER.info("update(bookSample) was started");
        LOGGER.debug("book={}", bookSample);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("bookId", bookSample.getId());
        sqlParameterSource.addValue("authors", bookSample.getAuthors());
        sqlParameterSource.addValue("title", bookSample.getTitle());
        sqlParameterSource.addValue("genre", bookSample.getGenre().ordinal());
        return namedParameterJdbcTemplate.update(updateSql, sqlParameterSource);
    }

    @Override
    public Integer delete(Integer bookId) {
        LOGGER.info("delete(BookDto) was started");
        LOGGER.debug("bookId={}", bookId);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("bookId", bookId);
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

    /**
     * Add the reader to this book
     * @param bookId book id in DB what adds the reader
     * @param readerId reader id of the reader
     * @return count rows (books) what add the reader
     */
    @Override
    public Integer addReaderForBook(Integer bookId, Integer readerId) {
        LOGGER.info("addReaderForBook(BookDto, reader) was started");
        LOGGER.debug("BookId={}, readerId={}", bookId, readerId);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("readerId", readerId);
        sqlParameterSource.addValue("bookId", bookId);
        return  namedParameterJdbcTemplate.update(addReaderForBookSql, sqlParameterSource.getValues());
    }

    /**
     * remove the reader with this id from all books
     * @param readerId reader id
     * @return count rows (books) what removed a reader
     */
    @Override
    public Integer removeReaderFromBooks(Integer readerId) {
        LOGGER.info("removeReaderFromBook(BookDto) was started");
        LOGGER.debug("readerId={}", readerId);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("readerId", readerId);
        return  namedParameterJdbcTemplate.update(removeReaderForBooksSql, sqlParameterSource.getValues());
    }

    @Override
    public List<BookSample> searchBooks(SearchBookSample bookSample) {
        LOGGER.info("searchBooks(bookSample) was started");
        LOGGER.debug("searchBookSample={}", bookSample);
        String sql = searchBooksSql;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("authors", "%" + bookSample.getAuthors() + "%");
        sqlParameterSource.addValue("title", "%" + bookSample.getTitle() + "%");
        if(bookSample.getGenre() != Genre.DEFAULT) {
            sqlParameterSource.addValue("genre", bookSample.getGenre().ordinal());
            sql = searchBooksWithGenreSql;
        }
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, catalogOfBooksMapper);
    }

    @Override
    public Integer removeFieldReaderFromBook(Integer bookId, Integer readerId) {
        LOGGER.info("removeFieldReaderFromBook was started");
        LOGGER.debug("bookId={}, readerId={}", bookId, readerId);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("bookId", bookId);
        sqlParameterSource.addValue("readerId", readerId);
        return namedParameterJdbcTemplate.update(removeReaderFromBookSql, sqlParameterSource);
    }

    @Override
    public void afterPropertiesSet() throws BeanCreationException {
        if(namedParameterJdbcTemplate == null){
            LOGGER.error("namedParametrJdbcTemplate is null");
            throw new BeanCreationException("Null JdbcTemplate on BookDaoSpringJdbc");
        }

        if(bookMapper == null){
            LOGGER.error("bookMapper is null");
            throw new BeanCreationException("Null bookMapper on BookDaoSpringJdbc");
        }

        if(catalogOfBooksMapper == null){
            LOGGER.error("catalogOfBooksMapper is null");
            throw new BeanCreationException("Null catalogOfBooksMapper on BookDaoSpringJdbc");
        }
    }
}
