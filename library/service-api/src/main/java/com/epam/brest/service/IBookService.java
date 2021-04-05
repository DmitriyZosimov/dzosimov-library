package com.epam.brest.service;

import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.service.exception.BookCreationException;

import java.util.List;

public interface IBookService {

    /**
     * Find all books
     * @return list of books
     */
    List<BookSample> findAll();

    /**
     * Connects a reader with book.
     * @param readerId id of the reader.
     * @param bookId id of the book.
     * @return true if it`s completed, and false if it`s not completed.
     */
    Boolean addReaderForBook(Integer readerId, Integer bookId);

    /**
     * Delete a book by id.
     * @param bookId id of the book.
     * @return true if it`s completed, and false if it`s not completed.
     */
    Boolean delete(Integer bookId);

    /**
     * Create and save a new book by a request model
     * @param bookSample the request model of a book.
     * @return true if it`s completed, and false if it`s not completed.
     */
    Boolean createBook(BookSample bookSample);

    /**
     * Search a book.
     * @param bookSample a model with parameters of book which is to be find.
     * @return List of found books.
     */
    List<BookSample> searchBooks(SearchBookSample bookSample);

    /**
     * Disconnect a reader and a book.
     * @param bookId id of the book.
     * @param readerId id of the reader.
     * @return true if it`s completed, and false if it`s not completed.
     */
    Boolean removeFieldReaderFromBook(Integer bookId, Integer readerId);

    /**
     * Update a book in data base.
     * @param bookSample model of the book which is to be updated.
     * @return true if it`s completed, and false if it`s not completed.
     */
    Boolean editBook(BookSample bookSample);

    /**
     * Find a book by id.
     * @param id book id.
     * @return book or null if the book not found.
     */
    BookSample findBookById(Integer id);
}
