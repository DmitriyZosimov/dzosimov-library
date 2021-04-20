package com.epam.brest.model.tools;

import com.epam.brest.model.Book;
import com.epam.brest.model.sample.BookSample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(BookMapper.class);

  public static BookSample getBookSample(Book book) {
    LOGGER.info("getBookSample");
    BookSample bookSample = new BookSample();
    bookSample.setId(book.getId());
    bookSample.setAuthors(book.getAuthors());
    bookSample.setTitle(book.getTitle());
    bookSample.setGenre(book.getGenre());
    return bookSample;
  }

  public static Book getBook(BookSample bookSample) {
    LOGGER.info("getBook");
    Book book = new Book();
    book.setAuthors(bookSample.getAuthors());
    book.setTitle(bookSample.getTitle());
    book.setGenre(bookSample.getGenre());
    book.setId(bookSample.getId());
    return book;
  }
}
