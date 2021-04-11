package com.epam.brest.dao.jdbc.tools;

import com.epam.brest.model.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindAllReader extends MappingSqlQuery<Reader> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindAllReader.class);

    public FindAllReader(DataSource dataSource, String sql){
        super(dataSource, sql);
        LOGGER.info("constructor FindAllReader(dataSource,sql) was started");
        LOGGER.debug("dataSource={}", dataSource);
        LOGGER.debug("sql={}",sql);
    }

    @Override
    protected Reader mapRow(ResultSet resultSet, int i) throws SQLException {
        LOGGER.info("mapRow(...) was started");
        Reader reader = new Reader();
        reader.setReaderId(resultSet.getInt("reader_id"));
        reader.setFirstName(resultSet.getString("first_name"));
        reader.setLastName(resultSet.getString("last_name"));
        reader.setPatronymic(resultSet.getString("patronymic"));
        reader.setDateOfRegistry(resultSet.getDate("date_of_registry").toLocalDate());
        reader.setActive(resultSet.getBoolean("active"));
        LOGGER.debug("mapRow(): reader={}", reader);
        return reader;
    }
}
