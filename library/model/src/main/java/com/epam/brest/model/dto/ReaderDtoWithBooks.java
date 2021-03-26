package com.epam.brest.model.dto;

import com.epam.brest.model.Book;

import java.util.List;

public class ReaderDtoWithBooks extends ReaderDto{

    private List<Book> books;

    public ReaderDtoWithBooks() {
        super();
    }

    public ReaderDtoWithBooks(List<Book> books) {
        super();
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ReaderDtoWithBooks that = (ReaderDtoWithBooks) o;

        return books != null ? books.equals(that.books) : that.books == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (books != null ? books.hashCode() : 0);
        return result;
    }
}
