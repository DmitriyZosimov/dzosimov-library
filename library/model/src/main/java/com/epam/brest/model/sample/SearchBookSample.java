package com.epam.brest.model.sample;

import com.epam.brest.model.Genre;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SearchBookSample {

    /*  позволяет:
        фамилии и инициалы ("Зосимов Д.А.")
        двойные фамилии ("Бут-гусаим Влад А.")
        несколько авторов через запятую ("Зосимов Д.А., Бут-гусаим Влад А.")
     */
    private final static String AUTHORS = "(([a-zA-Zа-яА-Я]+[\\.\\-]?(\\,)?)+(\\ )?)+";
    @Pattern(regexp = AUTHORS, message = "{pattern.book.authors}")
    @Size(min= 3, max = 60, message = "{size.book.authors}")
    private String authors;
    @Size(min= 3, max = 60, message = "{size.book.title}")
    private String title;
    @NotNull
    private Genre genre;

    public SearchBookSample() {
    }

    public SearchBookSample(@Pattern(regexp = AUTHORS) @Max(60) String authors, @Max(60) String title, @NotNull Genre genre) {
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

        SearchBookSample that = (SearchBookSample) o;

        if (authors != null ? !authors.equals(that.authors) : that.authors != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return genre == that.genre;
    }

    @Override
    public int hashCode() {
        int result = authors != null ? authors.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + genre.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SearchBookSample{" +
                "authors='" + authors + '\'' +
                ", title='" + title + '\'' +
                ", genre=" + genre +
                '}';
    }
}
