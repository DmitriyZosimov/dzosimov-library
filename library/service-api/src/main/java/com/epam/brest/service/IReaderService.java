package com.epam.brest.service;


import com.epam.brest.model.sample.ReaderSample;

public interface IReaderService {

    ReaderSample getProfile(Integer id);
    ReaderSample getProfileWithoutBooks(Integer id);
    ReaderSample createReader(ReaderSample readerSample);
    Boolean editProfile(ReaderSample readerSample);
    Boolean removeProfile(Integer id);
    Boolean restoreProfile(Integer id);


}
