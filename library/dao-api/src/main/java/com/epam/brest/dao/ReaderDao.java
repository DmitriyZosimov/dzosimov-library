package com.epam.brest.dao;

import com.epam.brest.model.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderDao {

    List<Reader> findAll();
    List<Reader> findAllActive();
    Optional<Reader> findReaderById(Integer id);
    Optional<Reader> findReaderByIdWithBooks(Integer id);
    Reader save(Reader reader);
    Integer update(Reader reader);
    Integer delete(Reader reader);
    Integer restore(Reader reader);
    Boolean exist(Integer readerId);
    Boolean exist(Integer readerId, boolean active);
}
