package com.epam.brest.model.tools;

import com.epam.brest.model.IReader;
import com.epam.brest.model.Reader;
import com.epam.brest.model.dto.ReaderDto;
import com.epam.brest.model.dto.ReaderDtoWithBooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReaderMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderMapper.class);

    public static ReaderDto getReaderDto(IReader reader){
        LOGGER.info("getReaderDto");
        ReaderDto readerDto = new ReaderDto();
        if(reader.getBooks() != null){
            LOGGER.info("getReaderDto - books not null");
            readerDto = new ReaderDtoWithBooks(reader.getBooks());
        }
        readerDto.setReaderId(reader.getReaderId());
        readerDto.setFirstName(reader.getFirstName());
        readerDto.setLastName(reader.getLastName());
        readerDto.setPatronymic(reader.getPatronymic());
        readerDto.setDateOfRegistry(reader.getDateOfRegistry());
        return readerDto;
    }

    public static IReader getReader(ReaderDto readerDto){
        LOGGER.info("getReader");
        IReader reader = new Reader();
        reader.setReaderId(readerDto.getReaderId());
        reader.setFirstName(readerDto.getFirstName());
        reader.setLastName(readerDto.getLastName());
        reader.setPatronymic(readerDto.getPatronymic());
        reader.setDateOfRegistry(readerDto.getDateOfRegistry());
        reader.setActive(true);
        if(readerDto instanceof ReaderDtoWithBooks){
            LOGGER.info("getReader - readerDto is ReaderDtoWithBooks.class");
            reader.setBooks(((ReaderDtoWithBooks) readerDto).getBooks());
        }
        return reader;
    }
}
