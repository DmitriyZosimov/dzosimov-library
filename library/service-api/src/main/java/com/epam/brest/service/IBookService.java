package com.epam.brest.service;

import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.service.exception.BookCreationException;

import java.util.List;

public interface IBookService {

    BookDto getBookDto();
    List<Book> findAll();
    boolean addReaderForBook(Integer readerId, Integer bookId);
    boolean delete(Integer bookId);
    Book createBook(BookDto bookDto) throws BookCreationException;
    List<Book> searchBooks(BookDto bookDto);
}
