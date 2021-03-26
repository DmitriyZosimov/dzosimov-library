package com.epam.brest.dao;

import com.epam.brest.model.Book;
import com.epam.brest.model.IReader;
import com.epam.brest.model.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    List<Book> findAll();
    Optional<Book> findBookById(Integer id);
    Book save(Book book);
    Integer update(Book book);
    Integer delete(Integer bookId);
    boolean exist(Book book);
    boolean existReader(Integer readerId);
    Integer addReaderForBook(Book book, Integer readerId);
    Integer removeReaderFromBook(Integer readerId);
    List<Book> searchBooks(BookDto bookDto);
}
