package com.epam.brest.dao;

import com.epam.brest.model.Reader;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReaderDao {

    /**
     * Find all readers
     * @return list of the readers
     */
    List<Reader> findAll();

    /**
     * Find only active readers
     * @return list of the readers
     */
    List<Reader> findAllActive();

    /**
     * Find the reader by identification without books
     * @param id identification of the reader
     * @return Optional<Reader>
     */
    Optional<Reader> findReaderById(Integer id);
    /**
     * Find the reader by identification with books
     * @param id identification of the reader
     * @return Optional<Reader>
     */
    Optional<Reader> findReaderByIdWithBooks(Integer id);

    /**
     * Save the reader
     * @param reader reader who is to be saved
     * @return the saved reader
     */
    Reader save(Reader reader);

    /**
     * Update the reader
     * @param reader reader who is to be updated
     * @return quantity of updated rows
     */
    Integer update(Reader reader);

    /**
     * Delete the reader
     * @param reader reader who is to be deleted
     * @return quantity of deleted rows
     */
    Integer delete(Reader reader);

    /**
     * Restore the reader
     * @param reader reader who is to be restored
     * @return quantity of updated rows
     */
    Integer restore(Reader reader);

    /**
     * Check if the reader exists
     * @param readerId identification of the reader
     * @return true if the reader exists, or false
     */
    Boolean exist(Integer readerId);

    /**
     * Check if the reader exists
     * @param readerId identification of the reader
     * @param active true if the reader must be removed, or false
     * @return true if the reader exists, or false
     */
    Boolean exist(Integer readerId, boolean active);

    /**
     * Find the readers by date
     * @param from date from
     * @param to date to
     * @return list of the found readers
     */
    List<Reader> findAllByDate(LocalDate from, LocalDate to);
}
