package com.epam.brest.service;

import com.epam.brest.model.dto.ReaderDto;
import com.epam.brest.service.exception.ReaderCreationException;
import com.epam.brest.service.exception.ReaderNotFoundException;

public interface IReaderService {

    ReaderDto getProfile(Integer id) throws ReaderNotFoundException;
    ReaderDto getProfileWithoutBooks(Integer id) throws ReaderNotFoundException;
    ReaderDto createReader(ReaderDto readerDto) throws ReaderCreationException;
    boolean editProfile(ReaderDto readerDto);
    boolean removeProfile(Integer id);
    boolean restoreProfile(Integer id);


}
