package com.epam.brest.service;

import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.service.exception.BookCreationException;

import java.util.List;

public interface IBookService {

    List<Book> findAll();
    boolean addReaderForBook(Integer readerId, Integer bookId);
    boolean delete(Integer bookId);
    boolean createBook(BookSample bookSample);
    List<Book> searchBooks(SearchBookSample bookSample);
    boolean removeFieldReaderFromBook(Integer bookId, Integer readerId);
    boolean editBook(BookSample bookSample);
    BookSample findBookById(Integer bookId);
}
