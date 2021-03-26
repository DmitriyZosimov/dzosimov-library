package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.ReaderDao;
import com.epam.brest.dao.jdbc.tools.*;
import com.epam.brest.model.IReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository("readerDaoSpringJdbc")
public class ReaderDaoSpringJdbc implements ReaderDao{

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderDaoSpringJdbc.class);

    @Value("${reader.jdbc.findAllReader}")
    private String findAllReaderSql;
    @Value("${reader.jdbc.findAllActiveReader}")
    private String findAllActiveReaderSql;
    @Value("${reader.jdbc.findReaderById}")
    private String findReaderByIdSql;
    @Value("${reader.jdbc.findReaderByIdWithBooks}")
    private String findReaderByIdWithBooksSql;
    @Value("${reader.jdbc.save}")
    private String saveSql;
    @Value("${reader.jdbc.update}")
    private String updateSql;
    @Value("${reader.jdbc.existAndActiveReader}")
    private String existReaderSql;

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource){
        LOGGER.debug("setDataSource: {}", dataSource);
        this.dataSource = dataSource;
    }

    /**
     * Find all readers without books in DB
     *
     * @return List readers
     */
    @Override
    public List<IReader> findAll() {
        LOGGER.info("findAll() was started");
        return new FindAllReader(dataSource, findAllReaderSql).execute();
    }

    /**
     * Find all active readers without books in DB
     *
     * @return List readers
     */
    @Override
    public List<IReader> findAllActive() {
        LOGGER.info("findAllActive() was started");
        return new FindAllReader(dataSource, findAllActiveReaderSql).execute();
    }

    /**
     * Find a reader without books by reader id
     * @param id - reader id
     * @return Optional of a reader if exist or null
     */
    @Override
    public Optional<IReader> findReaderById(Integer id) {
        LOGGER.info("findReaderById(id)  was started");
        LOGGER.debug("id={}", id);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("readerId", id);
        return Optional.ofNullable((IReader) new FindReaderById(dataSource, findReaderByIdSql).
                findObjectByNamedParam(sqlParameterSource.getValues()));
    }

    /**
     * Find a reader with books by id
     *
     * @param id - reader id
     * @return Optional of a reader if exist or null
     */
    @Override
    public Optional<IReader> findReaderByIdWithBooks(Integer id) {
        LOGGER.info("findReaderByIdWithBooks(id) was started");
        LOGGER.debug("id={}", id);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("readerId", id);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        FindReaderByIdWithBooks findReaderByIdWithBooks = new FindReaderByIdWithBooks();
        return Optional.ofNullable((IReader) jdbcTemplate.
                query(findReaderByIdWithBooksSql, sqlParameterSource.getValues(),
                        findReaderByIdWithBooks));
    }

    /**
     * Save a new reader
     * @param reader - a reader, who saves
     * @return saved reader with unique readerId
     */
    @Override
    public IReader save(IReader reader) {
        LOGGER.info("save(reader)  was started");
        LOGGER.debug("reader={}", reader);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("firstName", reader.getFirstName());
        sqlParameterSource.addValue("lastName", reader.getLastName());
        sqlParameterSource.addValue("patronymic", reader.getPatronymic());
        sqlParameterSource.addValue("dateOfRegistry", reader.getDateOfRegistry());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        new SaveReader(dataSource, saveSql).updateByNamedParam(sqlParameterSource.getValues(),keyHolder);
        reader.setReaderId(keyHolder.getKey().intValue());
        return reader;
    }

    /**
     * Update a reader
     * @param reader - reader, who updates
     * @return count updated rows(readers)
     */
    @Override
    public Integer update(IReader reader) {
        LOGGER.info("update(reader) was started");
        LOGGER.debug("reader={}", reader);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("firstName", reader.getFirstName());
        sqlParameterSource.addValue("lastName", reader.getLastName());
        sqlParameterSource.addValue("patronymic", reader.getPatronymic());
        sqlParameterSource.addValue("dateOfRegistry", reader.getDateOfRegistry());
        sqlParameterSource.addValue("active", reader.getActive());
        sqlParameterSource.addValue("readerId", reader.getReaderId());
        return new UpdateReader(dataSource, updateSql).updateByNamedParam(sqlParameterSource.getValues());
    }

    /**
     * Remove the reader (update active field to false)
     * @param reader - reader, who deletes
     * @return count updated rows (readers)
     */
    @Override
    public Integer delete(IReader reader) {
        LOGGER.info("delete(reader) was started");
        LOGGER.debug("reader={}", reader);
        reader.setActive(false);
        return update(reader);
    }

    /**
     * Remove the reader (update active field to true)
     * @param reader - reader, who restores
     * @return count updated rows (readers)
     */
    @Override
    public Integer restore(IReader reader) {
        LOGGER.info("restore(reader) was started");
        LOGGER.debug("reader={}", reader);
        reader.setActive(true);
        return update(reader);
    }

    /**
     * Check to exist an only active=true reader in DB
     *
     * @param readerId - reader id
     * @return true if reader is exist in DB, and false if reader not exist in DB
     */
    @Override
    public Boolean exist(Integer readerId) {
        LOGGER.info("exist(reader) was started");
        LOGGER.debug("readerId={}, active=true default", readerId);
        return exist(readerId, true);
    }

    /**
     * Check to exist a reader in DB
     *
     * @param readerId - reader id
     * @return true if reader is exist in DB, and false if reader not exist in DB
     */
    @Override
    public Boolean exist(Integer readerId, boolean active) {
        LOGGER.info("exist(reader, active) was started");
        LOGGER.debug("readerId={}, active={}", readerId, active);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("readerId", readerId);
        sqlParameterSource.addValue("active", active);
        return (Boolean) new ExistReader(dataSource, existReaderSql).
                findObjectByNamedParam(sqlParameterSource.getValues());
    }
}
