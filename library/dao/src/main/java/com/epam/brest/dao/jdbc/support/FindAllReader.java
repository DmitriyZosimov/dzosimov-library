package com.epam.brest.dao.jdbc.support;

import com.epam.brest.model.IReader;
import com.epam.brest.model.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindAllReader extends MappingSqlQuery<IReader> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindAllReader.class);

    private static final String FIND_ALL_READER =
            "SELECT * FROM lib_reader";
    private static final String FIND_ALL_ACTIVE_READER =
            "SELECT * FROM lib_reader WHERE active=true";

    public FindAllReader(DataSource dataSource){
        super(dataSource, FIND_ALL_ACTIVE_READER);
        LOGGER.info("constructor FindAllReader(dataSource) was started");
        LOGGER.debug("dataSource={}", dataSource);
    }

    public FindAllReader(DataSource dataSource, boolean withDeleted){
       super.setDataSource(dataSource);
        LOGGER.info("constructor FindAllReader(dataSource, withDeleted) was started");
        LOGGER.debug("dataSource={}", dataSource);
        LOGGER.debug("withDeleted={}", withDeleted);
       if(withDeleted){
           LOGGER.debug("FindAllReader used SQL:FIND_ALL_READER");
           super.setSql(FIND_ALL_READER);
       } else {
           LOGGER.debug("FindAllReader used SQL:FIND_ALL_ACTIVE_READER");
           super.setSql(FIND_ALL_ACTIVE_READER);
       }
    }

    @Override
    protected IReader mapRow(ResultSet resultSet, int i) throws SQLException {
        LOGGER.info("mapRow(...) was started");
        IReader reader = new Reader();
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
