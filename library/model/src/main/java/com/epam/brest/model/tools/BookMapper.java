package com.epam.brest.model.tools;

import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookMapper.class);

    public static BookDto getBookDto(Book book){
        LOGGER.info("getBookDto");
        BookDto bookDto = new BookDto();
        bookDto.setAuthors(book.getAuthors());
        bookDto.setTitle(book.getTitle());
        bookDto.setGenre(book.getGenre());
        return bookDto;
    }

    public static Book getBook(BookDto bookDto){
        LOGGER.info("getBook");
        Book book = new Book();
        book.setAuthors(bookDto.getAuthors());
        book.setTitle(bookDto.getTitle());
        book.setGenre(bookDto.getGenre());
        return book;
    }
}
