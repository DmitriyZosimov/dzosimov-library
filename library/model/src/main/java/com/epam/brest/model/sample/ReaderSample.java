package com.epam.brest.model.sample;

import com.epam.brest.model.Book;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class ReaderSample {

    //допускает двойные фамилии через "-"
    private final static String LAST_NAME = "[a-zA-Zа-яА-Я]+[\\-]?[a-zA-Zа-яА-Я]+";
    private final static String FIRST_NAME = "[a-zA-Zа-яА-Я]+";

    private Integer readerId;
    @NotBlank(message = "{not.blank}")
    @Pattern(regexp = FIRST_NAME, message = "{pattern.reader}")
    @Size(min = 3, max = 20, message = "{size.reader.firstName}")
    private String firstName;
    @NotBlank
    @Pattern(regexp = LAST_NAME, message = "{pattern.reader}")
    @Size(min = 3, max = 30, message = "{size.reader.lastName}")
    private String lastName;
    @Pattern(regexp = FIRST_NAME, message = "{pattern.reader}")
    @Size(max = 25, message = "{size.reader.patronymic}")
    private String patronymic;
    private LocalDate dateOfRegistry;
    private List<Book> books;

    public ReaderSample() {
    }

    public ReaderSample(@NotBlank(message = "{not.blank}") @Pattern(regexp = FIRST_NAME, message = "{pattern.reader}") @Size(min = 3, max = 20, message = "{size.reader.firstName}") String firstName, @NotBlank @Pattern(regexp = LAST_NAME, message = "{pattern.reader}") @Size(min = 3, max = 30, message = "{size.reader.lastName}") String lastName, @Pattern(regexp = FIRST_NAME, message = "{pattern.reader}") @Size(max = 25, message = "{size.reader.patronymic}") String patronymic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
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
