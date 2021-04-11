package com.epam.brest.service;

import com.epam.brest.dao.BookDao;
import com.epam.brest.dao.ReaderDao;
import com.epam.brest.model.Book;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.model.tools.BookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("bookServiceImp")
@Transactional
public class BookServiceImp implements IBookService {

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

    @Override
    public Boolean addReaderForBook(Integer readerId, Integer bookId) {
        LOGGER.info("addReaderForBook(readerId={}, bookId={})", readerId, bookId);
        if(!readerDao.exist(readerId)){
            LOGGER.info("reader not exist: readerId={}", readerId);
            return false;
        }
        int result = bookDao.addReaderForBook(bookId, readerId);
        return checkResult(bookId, result);
    }

    @Override
    public Boolean delete(Integer bookId) {
        LOGGER.info("delete(bookId={})", bookId);
        int result = bookDao.delete(bookId);
        return checkResult(bookId, result);
    }

    public Boolean createBook(BookSample bookSample){
        Book book = BookMapper.getBook(bookSample);
        book = bookDao.save(book);
        if(book.getId() == null){
            return false;
        }
        Integer result = bookDao.saveEntity(book.getId());
        if(result < 1){
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookSample> searchBooks(SearchBookSample bookSample) {
        List<BookSample> books = bookDao.searchBooks(bookSample);
        return books;
    }

    @Override
    public Boolean removeFieldReaderFromBook(Integer bookId, Integer readerId) {
        LOGGER.info("removeFieldReaderFromBook(bookId={}, readerId={})", bookId, readerId);
        int result = bookDao.removeFieldReaderFromBook(bookId, readerId);
        return checkResult(bookId, result);
    }

    @Override
    public Boolean editBook(BookSample bookSample) {
        LOGGER.info("editBook(bookSample={})", bookSample);
        int result = bookDao.update(bookSample);
        return checkResult(bookSample.getId(), result);
    }

    @Override
    public BookSample findBookById(Integer id) {
        LOGGER.info("findBookById(id={})", id);
        Optional<Book> optBook = bookDao.findBookById(id);
        return optBook.map(BookMapper::getBookSample).orElse(null);
    }

    private Boolean checkResult(Integer bookId, int result) {
        if(result == 1){
            LOGGER.info("The book(bookId={}) - all done correctly", bookId);
            return true;
        } else if(result > 1){
            LOGGER.warn("result={} > 1", result);
            return true;
        } else {
            LOGGER.info("The book(bookId={}) - some done wrong, maybe book is not found", bookId);
            return false;
        }
    }
}
