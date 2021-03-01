package com.epam.brest.model;

import java.time.LocalDate;
import java.util.List;

public class ReaderProxy implements IReader{
    private Integer readerId;
    private Reader reader;

    public ReaderProxy(Integer readerId){
        this.readerId = readerId;
    }

    @Override
    public Integer getReaderId() {
        return readerId;
    }

    @Override
    public void setReaderId(Integer readerId){
        this.readerId = readerId;
        lazyInitValue();
        reader.setReaderId(readerId);
    }

    @Override
    public String getFirstName() {
        lazyInitValue();
        return reader.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        lazyInitValue();
        reader.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        lazyInitValue();
        return  reader.getFirstName();
    }

    @Override
    public void setLastName(String lastName) {
        lazyInitValue();
        reader.setLastName(lastName);
    }

    @Override
    public String getPatronymic() {
        lazyInitValue();
        return reader.getPatronymic();
    }

    @Override
    public void setPatronymic(String patronymic) {
        lazyInitValue();
        reader.setPatronymic(patronymic);
    }

    @Override
    public LocalDate getDateOfRegistry() {
        lazyInitValue();
        return reader.getDateOfRegistry();
    }

    @Override
    public void setDateOfRegistry(LocalDate dateOfRegistry) {
        lazyInitValue();
        reader.setDateOfRegistry(dateOfRegistry);
    }

    @Override
    public List<Book> getBooks() {
        lazyInitValue();
        return reader.getBooks();
    }

    @Override
    public void setBooks(List<Book> books) {
        lazyInitValue();
        reader.setBooks(books);
    }

    @Override
    public Boolean getActive() {
        lazyInitValue();
        return reader.getActive();
    }

    @Override
    public void setActive(Boolean active) {
        lazyInitValue();
        reader.setActive(active);
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    //TODO: изменить ReaderDaoSpringJdbc на класс Service
    private void lazyInitValue(){
        this.reader = new Reader();
        //readerDaoSpringJdbc readerDaoSpringJdbc = new readerDaoSpringJdbc();
        //reader = readerDaoSpringJdbc.findReaderById(Integer readerId);
        //TODO: эту проверку вынести в Service class
        //if(reader == null){
        //  new throw NullPointerException("Reader not found by id:" + readerId
        //  + " or happened another problems");

    }
}
