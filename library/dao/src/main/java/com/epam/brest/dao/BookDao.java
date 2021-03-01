package com.epam.brest.dao;

import com.epam.brest.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookDao {

    List<Book> findAll();
    Optional<Book> findBookById(Integer id);
    Book save(Book book);
    Integer update(Book book);
    Integer delete(Book book);
    boolean exist(Book book);
}
