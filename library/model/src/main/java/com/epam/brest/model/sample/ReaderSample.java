package com.epam.brest.model.sample;

import com.epam.brest.model.Book;

import java.time.LocalDate;
import java.util.List;

public class ReaderSample {

    private Integer readerId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate dateOfRegistry;
    private List<Book> books;

    public ReaderSample() {
    }

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getDateOfRegistry() {
        return dateOfRegistry;
    }

    public void setDateOfRegistry(LocalDate dateOfRegistry) {
        this.dateOfRegistry = dateOfRegistry;
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

        ReaderSample that = (ReaderSample) o;

        if (readerId != null ? !readerId.equals(that.readerId) : that.readerId != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (patronymic != null ? !patronymic.equals(that.patronymic) : that.patronymic != null) return false;
        return dateOfRegistry != null ? dateOfRegistry.equals(that.dateOfRegistry) : that.dateOfRegistry == null;
    }

    @Override
    public int hashCode() {
        int result = readerId != null ? readerId.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
        result = 31 * result + (dateOfRegistry != null ? dateOfRegistry.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReaderSample{" +
                "readerId=" + readerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", dateOfRegistry=" + dateOfRegistry +
                '}';
    }
}
