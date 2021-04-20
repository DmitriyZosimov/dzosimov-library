package com.epam.brest.service;

import com.epam.brest.dao.BookDao;
import com.epam.brest.dao.ReaderDao;
import com.epam.brest.model.Book;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.model.tools.BookMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bookServiceImp")
@Transactional
public class BookServiceImp implements BookService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImp.class);

  private final BookDao bookDao;
  private final ReaderDao readerDao;

  @Autowired
  public BookServiceImp(BookDao bookDao, ReaderDao readerDao) {
    this.bookDao = bookDao;
    this.readerDao = readerDao;
  }

  @Transactional(readOnly = true)
  @Override
  public List<BookSample> findAll() {
    return bookDao.findAll();
  }

  @Transactional(readOnly = true)
  @Override
  public BookSample findBookById(Integer id) {
    LOGGER.info("findBookById(id={})", id);
    Optional<Book> optBook = bookDao.findBookById(id);
    return optBook.map(BookMapper::getBookSample).orElse(null);
  }

  @Override
  public Boolean createBook(BookSample bookSample) {
    Book book = bookDao.findBookByAuthorsAndTitleAndGenre(bookSample).orElse(null);
    if (book == null) {
      book = bookDao.save(BookMapper.getBook(bookSample));
      if (book.getId() == null) {
        return false;
      }
    }
    Integer result = bookDao.saveBookCopies(book.getId());
    return result > 0;
  }

  @Override
  public Boolean editBook(BookSample bookSample) {
    LOGGER.info("editBook(bookSample={})", bookSample);
    int result = bookDao.update(bookSample);
    return result > 0;
  }

  @Override
  public Boolean delete(Integer bookId) {
    LOGGER.info("delete(bookId={})", bookId);
    int result = bookDao.delete(bookId);
    return result > 0;
  }

  @Override
  public Boolean addBookForReader(Integer readerId, Integer bookId) {
    LOGGER.info("addBookForReader(readerId={}, bookId={})", readerId, bookId);
    if (!readerDao.isExistAmongActiveReaders(readerId)) {
      LOGGER.info("reader not exist: readerId={}", readerId);
      return false;
    }
    int result = bookDao.addBookForReader(bookId, readerId);
    return result > 0;
  }

  @Transactional(readOnly = true)
  @Override
  public List<BookSample> searchBooks(SearchBookSample bookSample) {
    return bookDao.searchBooks(bookSample);
  }

  @Override
  public Boolean removeBookFromReader(Integer bookId, Integer readerId) {
    LOGGER.info("removeBookFromReader(bookId={}, readerId={})", bookId, readerId);
    int result = bookDao.removeBookFromReader(bookId, readerId);
    return result > 0;
  }
}
