package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.ReaderDao;
import com.epam.brest.dao.jdbc.support.*;
import com.epam.brest.model.IReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource){
        LOGGER.debug("setDataSource: {}", dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public List<IReader> findAll() {
        LOGGER.info("findAll() was started");
        return new FindAllReader(dataSource, true).execute();
    }

    @Override
    public List<IReader> findAllActive() {
        LOGGER.info("findAllActive() was started");
        return new FindAllReader(dataSource).execute();
    }

    @Override
    public Optional<IReader> findReaderById(Integer id) {
        LOGGER.info("findReaderById(id)  was started");
        LOGGER.debug("id={}", id);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("readerId", id);
        return Optional.ofNullable((IReader) new FindReaderById(dataSource).
                findObjectByNamedParam(sqlParameterSource.getValues()));
    }

    @Override
    public Optional<IReader> findReaderByIdWithBooks(Integer id) {
        LOGGER.info("findReaderByIdWithBooks(id) was started");
        LOGGER.debug("id={}", id);
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("readerId", id);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        FindReaderByIdWithBooks findReaderByIdWithBooks = new FindReaderByIdWithBooks();
        return Optional.ofNullable((IReader) jdbcTemplate.
                query(FindReaderByIdWithBooks.getFindReaderByIdWithBooks(), sqlParameterSource.getValues(),
                        findReaderByIdWithBooks));
    }

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
        new SaveReader(dataSource).updateByNamedParam(sqlParameterSource.getValues(),keyHolder);
        reader.setReaderId(keyHolder.getKey().intValue());
        return reader;
    }

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
        return new UpdateReader(dataSource).updateByNamedParam(sqlParameterSource.getValues());
    }

    @Override
    public Integer delete(IReader reader) {
        LOGGER.info("delete(reader) was started");
        LOGGER.debug("reader={}", reader);
        reader.setActive(false);
        return update(reader);
    }

    @Override
    public Integer restore(IReader reader) {
        LOGGER.info("restore(reader) was started");
        LOGGER.debug("reader={}", reader);
        reader.setActive(true);
        return update(reader);
    }

    @Override
    public boolean exist(IReader reader) {
        LOGGER.info("exist(reader) was started");
        LOGGER.debug("reader={}", reader);
        MapSqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("readerId", reader.getReaderId());
        return (boolean) new ExistReader(dataSource).findObjectByNamedParam(sqlParameterSource.getValues());
    }

}
