package com.epam.brest.dao;

import com.epam.brest.model.Book;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import java.util.List;
import java.util.Optional;

public interface BookDao {

  /**
   * Find all books
   *
   * @return list of books
   */
  List<BookSample> findAll();

  /**
   * Find a book by identification
   *
   * @param id identification of the book
   * @return Optional<BooK>
   */
  Optional<Book> findBookById(Integer id);

  /**
   * Find a book by authors, title and genre
   *
   * @param bookSample sample of the book
   * @return Optional<BooK>
   */
  Optional<Book> findBookByAuthorsAndTitleAndGenre(BookSample bookSample);

  /**
   * Save the book
   *
   * @param book book which is to be saved
   * @return quantity of rows is added
   */
  Book save(Book book);

  /**
   * Save entity of the book after book saving
   *
   * @param id identification of the book
   * @return quantity of rows is added
   */
  public Integer saveBookCopies(Integer id);

  /**
   * Update the book
   *
   * @param bookSample sample of the book
   * @return quantity of rows is updated
   */
  Integer update(BookSample bookSample);

  /**
   * Delete the book by identification
   *
   * @param bookId identification of the book
   * @return quantity of rows is deleted
   */
  Integer delete(Integer bookId);

  /**
   * Tie the reader and the entity of the book
   *
   * @param bookId   identification of the book
   * @param readerId identification of the book
   * @return quantity of rows is added
   */
  Integer addBookForReader(Integer bookId, Integer readerId);

  /**
   * Remove all ties of the reader and books
   *
   * @param readerId identification of the reader
   * @return quantity of rows is removed
   */
  Integer removeAllBooksFromReader(Integer readerId);

  /**
   * Find books that likes the request book sample
   *
   * @param bookSample sample of the book
   * @return list of found books
   */
  List<BookSample> searchBooks(SearchBookSample bookSample);

  /**
   * Remove tie of the reader and the book
   *
   * @param bookId   identification of the book
   * @param readerId identification of the reader
   * @return quantity of rows is removed
   */
  Integer removeBookFromReader(Integer bookId, Integer readerId);
}
