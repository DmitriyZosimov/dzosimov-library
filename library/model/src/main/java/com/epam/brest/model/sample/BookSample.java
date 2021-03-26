package com.epam.brest.model.sample;

import com.epam.brest.model.Genre;

public class BookSample {
    //TODO: validator
    private Integer id;
    private String authors;
    private String title;
    private Genre genre;

    public BookSample() {
    }

    public BookSample(String authors, String title, Genre genre) {
        this.authors = authors;
        this.title = title;
        this.genre = genre;
    }

    public BookSample(Integer id, String authors, String title, Genre genre) {
        this.id = id;
        this.authors = authors;
        this.title = title;
        this.genre = genre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

        BookSample that = (BookSample) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (authors != null ? !authors.equals(that.authors) : that.authors != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return genre == that.genre;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BookSample{" +
                "id=" + id +
                ", authors='" + authors + '\'' +
                ", title='" + title + '\'' +
                ", genre=" + genre +
                '}';
    }
}
