package com.epam.brest.model;

import java.time.LocalDate;
import java.util.List;

public interface IReader {

    public Integer getReaderId();
    public void setReaderId(Integer readerId);
    public String getFirstName();
    public void setFirstName(String firstName);
    public String getLastName();
    public void setLastName(String lastName);
    public String getPatronymic();
    public void setPatronymic(String patronymic);
    public LocalDate getDateOfRegistry();
    public void setDateOfRegistry(LocalDate dateOfRegistry);
    public List<Book> getBooks();
    public void setBooks(List<Book> books);
    public Boolean getActive();
    public void setActive(Boolean active);
}
