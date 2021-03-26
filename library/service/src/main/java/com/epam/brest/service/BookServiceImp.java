package com.epam.brest.service;

import com.epam.brest.dao.BookDao;
import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.tools.BookMapper;
import com.epam.brest.service.exception.BookCreationException;
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

    @Autowired
    public BookServiceImp(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    /**
     * Create BookDto
     * @return BookDto
     */
    @Override
    public BookSample getBookSample() {
        LOGGER.info("Create bookSample");
        return new BookSample();
    }

    /**
     * Find all books
     * @return list of books
     */
    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public boolean addReaderForBook(Integer readerId, Integer bookId) {
        LOGGER.info("addReaderForBook(readerId={}, bookId={})", readerId, bookId);
        int result = bookDao.addReaderForBook(bookId, readerId);
        return checkResult(bookId, result);
    }

    @Override
    public boolean delete(Integer bookId) {
        LOGGER.info("delete(bookId={})", bookId);
        int result = bookDao.delete(bookId);
        return checkResult(bookId, result);
    }

    private boolean checkResult(Integer bookId, int result) {
        if(result == 1){
            LOGGER.info("The book(bookId={}) - all done correctly", bookId);
            return true;
        } else if(result > 1){
            LOGGER.warn("result={} > 1", result);
            return true;
        } else {
            LOGGER.info("The book(bookId={}) - some done wrong", bookId);
            return false;
        }
    }

    /**
     * Create a new book
     * @param bookSample model of a Book
     * @return book created book
     */
    public Book createBook(BookSample bookSample) throws BookCreationException {
        Book book = BookMapper.getBook(bookSample);
        book = bookDao.save(book);
        if(book.getId() == null){
            throw new BookCreationException("book id must be not null");
        }
        return book;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> searchBooks(BookSample bookSample) {
        List<Book> books = bookDao.searchBooks(bookSample);
        return books;
    }

    @Override
    public boolean removeFieldReaderFromBook(Integer bookId, Integer readerId) {
        LOGGER.info("removeFieldReaderFromBook(bookId={}, readerId={})", bookId, readerId);
        int result = bookDao.removeFieldReaderFromBook(bookId, readerId);
        return checkResult(bookId, result);
    }

    @Transactional(readOnly = true)
    public Book findBookById(Integer id){
        Optional<Book> opt = bookDao.findBookById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        return null;
    }
}
