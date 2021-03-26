package com.epam.brest.model.dto;

import com.epam.brest.model.Genre;

public class BookDto {

    private String authors;
    private String title;
    private Genre genre;

    public BookDto(){}

    public BookDto(String authors, String title, Genre genre){
        this.authors = authors;
        this.title = title;
        this.genre = genre;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookDto bookDto = (BookDto) o;

        if (!authors.equals(bookDto.authors)) return false;
        if (!title.equals(bookDto.title)) return false;
        return genre == bookDto.genre;
    }

    @Override
    public int hashCode() {
        int result = authors.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + genre.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "authors='" + authors + '\'' +
                ", title='" + title + '\'' +
                ", genre=" + genre +
                '}';
    }
}
