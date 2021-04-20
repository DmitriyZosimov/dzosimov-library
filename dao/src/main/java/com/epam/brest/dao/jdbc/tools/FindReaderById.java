package com.epam.brest.dao.jdbc.tools;

import com.epam.brest.model.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

public class FindReaderById extends MappingSqlQuery<Reader> {

  private static final Logger LOGGER = LoggerFactory.getLogger(FindReaderById.class);

  public FindReaderById(DataSource ds, String sql) {
    super(ds, sql);
    super.declareParameter(new SqlParameter("readerId", Types.INTEGER));
    LOGGER.info("constructor FindReaderById(dataSource) was started");
    LOGGER.debug("dataSource={}", ds);
    LOGGER.debug("sql={}", sql);
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
