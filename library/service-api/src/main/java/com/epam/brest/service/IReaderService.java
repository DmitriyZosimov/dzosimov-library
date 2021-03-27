package com.epam.brest.service;


import com.epam.brest.model.sample.ReaderSample;

public interface IReaderService {

    ReaderSample getProfile(Integer id);
    ReaderSample getProfileWithoutBooks(Integer id);
    ReaderSample createReader(ReaderSample readerDto);
    boolean editProfile(ReaderSample readerDto);
    boolean removeProfile(Integer id);
    boolean restoreProfile(Integer id);


}
