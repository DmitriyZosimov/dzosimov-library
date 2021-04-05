package com.epam.brest.service;

import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.service.exception.BookCreationException;

import java.util.List;

public interface IBookService {

    List<BookSample> findAll();
    Boolean addReaderForBook(Integer readerId, Integer bookId);
    Boolean delete(Integer bookId);
    Boolean createBook(BookSample bookSample);
    List<BookSample> searchBooks(SearchBookSample bookSample);
    Boolean removeFieldReaderFromBook(Integer bookId, Integer readerId);
    Boolean editBook(BookSample bookSample);
}
