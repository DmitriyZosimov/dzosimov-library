package com.epam.brest.model.dto;

import java.time.LocalDate;

public class ReaderDto {

    private Integer readerId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate dateOfRegistry;

    public ReaderDto(){

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReaderDto readerDto = (ReaderDto) o;

        if (!readerId.equals(readerDto.readerId)) return false;
        if (!firstName.equals(readerDto.firstName)) return false;
        if (!lastName.equals(readerDto.lastName)) return false;
        if (patronymic != null ? !patronymic.equals(readerDto.patronymic) : readerDto.patronymic != null) return false;
        return dateOfRegistry.equals(readerDto.dateOfRegistry);
    }

    @Override
    public int hashCode() {
        int result = readerId.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
        result = 31 * result + dateOfRegistry.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ReaderDto{" +
                "readerId=" + readerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", dateOfRegistry=" + dateOfRegistry +
                '}';
    }
}
