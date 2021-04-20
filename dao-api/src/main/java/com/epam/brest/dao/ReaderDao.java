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
     * Set a reader not active
     * @param reader reader who is to be not active
     * @return quantity of deleted rows
     */
    Integer changeReaderToNoActive(Reader reader);

    /**
     * Set a reader active
     * @param reader reader who is to be active
     * @return quantity of updated rows
     */
    Integer changeReaderToActive(Reader reader);

    /**
     * Check if the reader is exist among active readers
     * @param readerId identification of the reader
     * @return true if the reader exists, or false
     */
    Boolean isExistAmongActiveReaders(Integer readerId);

    /**
     * Check if the reader is exist among readers
     * @param readerId identification of the reader
     * @param active true if the reader must be no active, or false
     * @return true if the reader exists, or false
     */
    Boolean isExistAmongReadersByActive(Integer readerId, boolean active);

    /**
     * Find the readers by date
     * @param from date from
     * @param to date to
     * @return list of the found readers
     */
    List<Reader> findAllByDate(LocalDate from, LocalDate to);
}
