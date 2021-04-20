package com.epam.brest.service;

import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import java.util.List;

public interface BookService {

  /**
   * Find all books
   *
   * @return list of books
   */
  List<BookSample> findAll();

  /**
   * Find a book by identification.
   *
   * @param id identification of a book.
   * @return book or null if the book is not found.
   */
  BookSample findBookById(Integer id);

  /**
   * Update a book in data base.
   *
   * @param bookSample sample of the book which is to be updated.
   * @return true if executed and false if not executed.
   */
  Boolean editBook(BookSample bookSample);


  /**
   * Create and save a new book by a request model
   *
   * @param bookSample sample of a book.
   * @return true if executed and false if not executed.
   */
  Boolean createBook(BookSample bookSample);

  /**
   * Delete a book by id.
   *
   * @param bookId identification of the book.
   * @return true if executed and false if not executed.
   */
  Boolean delete(Integer bookId);

  /**
   * Connects a reader with book.
   *
   * @param readerId identification of the reader.
   * @param bookId   identification of the book.
   * @return true if executed and false if not executed.
   */
  Boolean addBookForReader(Integer readerId, Integer bookId);

  /**
   * Search a book.
   *
   * @param bookSample sample with parameters of a book which is to be find.
   * @return List of found books.
   */
  List<BookSample> searchBooks(SearchBookSample bookSample);

  /**
   * Delete a tie of reader and book
   *
   * @param bookId   identification of a book
   * @param readerId identification of a reader
   * @return true if executed and false if not executed
   */
  Boolean removeBookFromReader(Integer bookId, Integer readerId);

}
