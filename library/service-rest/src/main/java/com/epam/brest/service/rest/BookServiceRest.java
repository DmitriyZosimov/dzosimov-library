package com.epam.brest.service.rest;

import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.service.IBookService;

import java.util.List;

public class BookServiceRest implements IBookService {
    @Override
    public List<BookSample> findAll() {
        return null;
    }

    @Override
    public boolean addReaderForBook(Integer readerId, Integer bookId) {
        return false;
    }

    @Override
    public boolean delete(Integer bookId) {
        return false;
    }

    @Override
    public boolean createBook(BookSample bookSample) {
        return false;
    }

    @Override
    public List<BookSample> searchBooks(SearchBookSample bookSample) {
        return null;
    }

    @Override
    public boolean removeFieldReaderFromBook(Integer bookId, Integer readerId) {
        return false;
    }

    @Override
    public boolean editBook(BookSample bookSample) {
        return false;
    }

    @Override
    public BookSample findBookById(Integer bookId) {
        return null;
    }
}
