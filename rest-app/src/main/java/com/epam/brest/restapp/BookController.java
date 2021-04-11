package com.epam.brest.restapp;

import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
@Validated
@RestController
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final IBookService bookService;

    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Find all
     * @return list of books
     */
    @GetMapping(value = "/book",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookSample>> findAll(){
        LOGGER.info("findAll()");
        List<BookSample> books = bookService.findAll();
        return new ResponseEntity<List<BookSample>>(books, HttpStatus.OK);
    }

    /**
     * Find a book by identification
     * @param bookId identification of a book
     * @return a model of the book or {@link HttpStatus} NOT FOUND
     */
    @GetMapping(value = "/book/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookSample> findBookById(@PathVariable("id") @Min(1) Integer bookId){
        LOGGER.info("findBookById(id={})", bookId);
        BookSample bookSample = bookService.findBookById(bookId);
        return bookSample != null ? new ResponseEntity<BookSample>(bookSample, HttpStatus.OK)
                : new ResponseEntity("The book was not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Tie reader and book
     * @param bookId identification of a book
     * @param readerId identification of a reader
     * @return true if executed and false if not executed
     */
    @GetMapping(value = "/book/{bookId}/reader/{readerId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> addReaderForBook(@PathVariable("bookId") @Min(1) Integer bookId,
                                                    @PathVariable("readerId") @Min(1) Integer readerId){
        LOGGER.info("addReaderForBook(bookId={}, readerId={})", bookId, readerId);
        Boolean result = bookService.addReaderForBook(readerId, bookId);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    /**
     * Search books
     * @param bookSample sample of a book
     * @return list of found books
     */
    @PostMapping(value = "/book/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookSample>> searchBooks(@Valid @RequestBody SearchBookSample bookSample){
        LOGGER.info("searchBooks(bookSample={})", bookSample);
        List<BookSample> books = bookService.searchBooks(bookSample);
        return new ResponseEntity<List<BookSample>>(books, HttpStatus.OK);
    }

    /**
     * Save a book
     * @param bookSample sample of a book
     * @return true, if executed, or {@link HttpStatus} BAD REQUEST
     */
    @PostMapping(value = "/book",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createBook(@Valid @RequestBody BookSample bookSample){
        LOGGER.info("createBook(bookSample={})", bookSample);
        Boolean result = bookService.createBook(bookSample);
        return result ?  new ResponseEntity<Boolean>(result, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Update a book
     * @param bookSample sample of a book
     * @return true, if executed, or {@link HttpStatus} BAD REQUEST
     */
    @PutMapping(value = "/book",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> editBook(@Valid @RequestBody BookSample bookSample){
        LOGGER.info("editBook(bookSample={})", bookSample);
        Boolean result = bookService.editBook(bookSample);
        return result ?  new ResponseEntity<Boolean>(result, HttpStatus.OK)
                : new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * Delete a book
     * @param bookId identification of a book
     * @return true, if executed, or {@link HttpStatus} BAD REQUEST
     */
    @DeleteMapping(value = "/book/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteBook(@PathVariable("id") @Min(1) Integer bookId){
        LOGGER.info("deleteBook(bookId={})", bookId);
        Boolean result = bookService.delete(bookId);
        return result ?  new ResponseEntity<Boolean>(result, HttpStatus.OK)
                : new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * Delete a tie of reader and book
     * @param bookId identification of a book
     * @param readerId identification of a reader
     * @return true if executed and false if not executed
     */
    @DeleteMapping(value = "/book/{bookId}/reader/{readerId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> removeFieldReaderFromBook(@PathVariable("bookId") @Min(1) Integer bookId,
                                                             @PathVariable("readerId") @Min(1) Integer readerId){
        LOGGER.info("removeFieldReaderFromBook(bookId={}, readerId={})", bookId, readerId);
        Boolean result = bookService.removeFieldReaderFromBook(bookId, readerId);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
}
