package com.epam.brest.model.tools;

import com.epam.brest.model.Reader;
import com.epam.brest.model.sample.ReaderSample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReaderMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReaderMapper.class);

  public static ReaderSample getReaderSample(Reader reader) {
    LOGGER.info("getReaderSample");
    ReaderSample readerSample = new ReaderSample();
    readerSample.setReaderId(reader.getReaderId());
    readerSample.setFirstName(reader.getFirstName());
    readerSample.setLastName(reader.getLastName());
    readerSample.setPatronymic(reader.getPatronymic());
    readerSample.setDateOfRegistry(reader.getDateOfRegistry());
    readerSample.setBooks(reader.getBooks());
    return readerSample;
  }

  public static Reader getReader(ReaderSample readerSample) {
    LOGGER.info("getReader");
    Reader reader = new Reader();
    reader.setReaderId(readerSample.getReaderId());
    reader.setFirstName(readerSample.getFirstName());
    reader.setLastName(readerSample.getLastName());
    reader.setPatronymic(readerSample.getPatronymic());
    reader.setDateOfRegistry(readerSample.getDateOfRegistry());
    reader.setActive(true);
    reader.setBooks(readerSample.getBooks());
    return reader;
  }
}
