package com.epam.brest.dao;

import com.epam.brest.model.Book;
import com.epam.brest.model.IReader;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    List<Book> findAll();
    Optional<Book> findBookById(Integer id);
    Book save(Book book);
    Integer update(Book book);
    Integer delete(Book book);
    boolean exist(Book book);
    Integer addReaderForBook(Book book, IReader reader);
    Integer removeReaderFromBook(Book book);
}
