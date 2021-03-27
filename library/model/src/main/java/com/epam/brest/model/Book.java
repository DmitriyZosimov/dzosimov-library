package com.epam.brest.model;

public class Book {

    private Integer id;
    private String authors;
    private String title;
    private Genre genre;
    private Integer readerId;

    public Book() {
    }

    public Book(String authors, String title, Genre genre) {
        this.authors = authors;
        this.title = title;
        this.genre = genre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

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

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!id.equals(book.id)) return false;
        if (!authors.equals(book.authors)) return false;
        if (!title.equals(book.title)) return false;
        return genre == book.genre;
    }

    @Override
    public int hashCode() {
        int result = 17 * id.hashCode();
        result = 31 * result + authors.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + genre.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", authors='" + authors + '\'' +
                ", title='" + title + '\'' +
                ", genre=" + genre +
                ", readerId=" + readerId +
                '}';
    }
}
