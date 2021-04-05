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
     * @param readerId identification of the reader.
     * @param bookId identification of the book.
     * @return true if executed and false if not executed.
     */
    Boolean addReaderForBook(Integer readerId, Integer bookId);

    /**
     * Delete a book by id.
     * @param bookId identification of the book.
     * @return true if executed and false if not executed.
     */
    Boolean delete(Integer bookId);

    /**
     * Create and save a new book by a request model
     * @param bookSample sample of a book.
     * @return true if executed and false if not executed.
     */
    Boolean createBook(BookSample bookSample);

    /**
     * Search a book.
     * @param bookSample sample with parameters of a book which is to be find.
     * @return List of found books.
     */
    List<BookSample> searchBooks(SearchBookSample bookSample);

    /**
     * Delete a tie of reader and book
     * @param bookId identification of a book
     * @param readerId identification of a reader
     * @return true if executed and false if not executed
     */
    Boolean removeFieldReaderFromBook(Integer bookId, Integer readerId);

    /**
     * Update a book in data base.
     * @param bookSample sample of the book which is to be updated.
     * @return true if executed and false if not executed.
     */
    Boolean editBook(BookSample bookSample);

    /**
     * Find a book by identification.
     * @param id identification of a book.
     * @return book or null if the book is not found.
     */
    BookSample findBookById(Integer id);
}
