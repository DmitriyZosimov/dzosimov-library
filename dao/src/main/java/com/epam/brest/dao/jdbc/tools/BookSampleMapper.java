package com.epam.brest.dao.jdbc.tools;

import com.epam.brest.model.Genre;
import com.epam.brest.model.sample.BookSample;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BookSampleMapper implements RowMapper<BookSample> {

  private final Logger LOGGER = LoggerFactory.getLogger(BookSampleMapper.class);

  @Override
  public BookSample mapRow(ResultSet rs, int rowNum) throws SQLException {
    BookSample bookSample = new BookSample();
    bookSample.setId(rs.getInt("book_id"));
    bookSample.setAuthors(rs.getString("authors"));
    bookSample.setTitle(rs.getString("title"));
    Genre genre = Genre.values()[rs.getInt("genre")];
    bookSample.setGenre(genre);
    bookSample.setQuantity(rs.getInt("quantity"));
    LOGGER.info("CatalogOfBooksMapper.class mapRow(resultSet, i) was started");
    LOGGER.debug("BookSample={}", bookSample);
    return bookSample;
  }
}
