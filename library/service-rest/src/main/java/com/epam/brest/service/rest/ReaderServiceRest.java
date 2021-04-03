package com.epam.brest.service.rest;

import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.service.IReaderService;

public class ReaderServiceRest implements IReaderService {
    @Override
    public ReaderSample getProfile(Integer id) {
        return null;
    }

    @Override
    public ReaderSample getProfileWithoutBooks(Integer id) {
        return null;
    }

    @Override
    public ReaderSample createReader(ReaderSample readerDto) {
        return null;
    }

    @Override
    public boolean editProfile(ReaderSample readerDto) {
        return false;
    }

    @Override
    public boolean removeProfile(Integer id) {
        return false;
    }

    @Override
    public boolean restoreProfile(Integer id) {
        return false;
    }
}
