package com.epam.brest.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "lib_reader")
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_id", nullable = false, unique = true)
    private Integer readerId;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "patronymic")
    private String patronymic;
    @Column(name = "date_of_registry", nullable = false)
    private LocalDate dateOfRegistry;
    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private Boolean active;
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "reader")
    private List<Book> books;

    public Reader(){}

    public Reader(String firstName, String lastName, String patronymic,
                  LocalDate dateOfRegistry, Boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.dateOfRegistry = dateOfRegistry;
        this.active = active;
    }

    public Reader(String firstName, String lastName, String patronymic,
                  LocalDate dateOfRegistry) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.dateOfRegistry = dateOfRegistry;
        this.active = true;
    }

    public Integer getReaderId() {
        return readerId;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reader reader = (Reader) o;

        if (!readerId.equals(reader.readerId)) return false;
        if (!firstName.equals(reader.firstName)) return false;
        if (!lastName.equals(reader.lastName)) return false;
        if (patronymic != null ? !patronymic.equals(reader.patronymic) : reader.patronymic != null) return false;
        return dateOfRegistry.equals(reader.dateOfRegistry);
    }

    @Override
    public int hashCode() {
        int result = 17 * readerId.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
        result = 31 * result + dateOfRegistry.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "readerId=" + readerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", dateOfRegistry=" + dateOfRegistry +
                ", books=" + books +
                '}';
    }
}
