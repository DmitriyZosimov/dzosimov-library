package com.epam.brest.dao;

import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    List<Book> findAll();
    Optional<Book> findBookById(Integer id);
    Book save(Book book);
    Integer update(BookSample bookSample);
    Integer delete(Integer bookId);
    boolean exist(Book book);
    Integer addReaderForBook(Integer bookId, Integer readerId);
    Integer removeReaderFromBooks(Integer readerId);
    List<Book> searchBooks(SearchBookSample bookSample);
    Integer removeFieldReaderFromBook(Integer bookId, Integer readerId);
}
