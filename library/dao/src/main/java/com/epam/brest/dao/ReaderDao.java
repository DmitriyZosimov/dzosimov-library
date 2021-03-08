package com.epam.brest.dao;

import com.epam.brest.model.IReader;

import java.util.List;
import java.util.Optional;

public interface ReaderDao {

    List<IReader> findAll();
    List<IReader> findAllActive();
    Optional<IReader> findReaderById(Integer id);
    Optional<IReader> findReaderByIdWithBooks(Integer id);
    IReader save(IReader reader);
    Integer update(IReader reader);
    Integer delete(IReader reader);
    Integer restore(IReader reader);
    boolean exist(IReader reader);
}
