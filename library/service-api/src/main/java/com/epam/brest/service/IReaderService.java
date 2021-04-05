package com.epam.brest.service;


import com.epam.brest.model.sample.ReaderSample;

public interface IReaderService {

    /**
     * Find a reader.
     * @param id reader id.
     * @return a model of the reader with books.
     */
    ReaderSample getProfile(Integer id);
    /**
     * Find a reader.
     * @param id reader id.
     * @return a model of the reader without books.
     */
    ReaderSample getProfileWithoutBooks(Integer id);
    /**
     * Create and save a reader.
     * @param readerSample a model of the reader.
     * @return the model of the saved reader.
     */
    ReaderSample createReader(ReaderSample readerSample);

    /**
     * Update a reader.
     * @param readerSample a model of the reader who is to updated.
     * @return true if it`s completed, and false if it`s not completed.
     */
    Boolean editProfile(ReaderSample readerSample);

    /**
     * Set a reader not active
     * @param id reader id.
     * @return true if it`s completed, and false if it`s not completed.
     */
    Boolean removeProfile(Integer id);

    /**
     * Set a reader active
     * @param id reader id.
     * @return true if it`s completed, and false if it`s not completed.
     */
    Boolean restoreProfile(Integer id);


}
