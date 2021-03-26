package com.epam.brest.service;

import com.epam.brest.dao.BookDao;
import com.epam.brest.dao.ReaderDao;
import com.epam.brest.model.Book;
import com.epam.brest.model.IReader;
import com.epam.brest.model.dto.ReaderDto;
import com.epam.brest.model.dto.ReaderDtoWithBooks;
import com.epam.brest.model.tools.ReaderMapper;
import com.epam.brest.service.exception.ReaderCreationException;
import com.epam.brest.service.exception.ReaderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service("readerService")
public class ReaderServiceImp implements IReaderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderServiceImp.class);

    private final ReaderDao readerDao;
    private final BookDao bookDao;

    @Autowired
    public ReaderServiceImp(ReaderDao readerDao, BookDao bookDao) {
        this.readerDao = readerDao;
        this.bookDao = bookDao;
    }

    /**
     * Find a profile of a reader by id
     * @param id - reader id
     * @return reader if exist or new ReaderDtoWithBooks();
     */
    @Override
    public ReaderDto getProfile(Integer id) throws ReaderNotFoundException {
        LOGGER.info("getProfile");
        Optional<IReader> readerOpt = readerDao.findReaderByIdWithBooks(id);
        if(readerOpt.isEmpty()){
            throw new ReaderNotFoundException("reader with id=" + id + " not found");
        }
        ReaderDto readerDto = ReaderMapper.getReaderDto(readerOpt.get());
        return readerDto;
    }

    /**
     * Find a profile of a reader by id
     * @param id - reader id
     * @return reader if exist or new ReaderTto()
     */
    @Override
    public ReaderDto getProfileWithoutBooks(Integer id) throws ReaderNotFoundException {
        LOGGER.info("getProfileWithoutBooks");
        Optional<IReader> readerOpt = readerDao.findReaderById(id);
        if(readerOpt.isEmpty()){
            throw new ReaderNotFoundException("reader with id=" + id + " not found");
        }
        ReaderDto readerDto = ReaderMapper.getReaderDto(readerOpt.get());
        return readerDto;
    }

    /**
     * Add a new reader
     * @param readerDto reader
     * @return saved ReaderDto
     */
    @Override
    public ReaderDto createReader(ReaderDto readerDto) throws ReaderCreationException {
        LOGGER.info("createReader");
        LOGGER.debug("readerDto={}", readerDto);
        if (readerDto == null){
            LOGGER.info("reader is not added, because a request reader is null");
            throw new ReaderCreationException("ReaderDto is null");
        }
        IReader reader = ReaderMapper.getReader(readerDto);
        reader.setDateOfRegistry(LocalDate.now());
        reader = readerDao.save(reader);
        if(reader.getReaderId() != null){
            LOGGER.info("reader was added");
            return ReaderMapper.getReaderDto(reader);
        } else {
            LOGGER.warn("happened some problem, a reader was not added");
            throw new ReaderCreationException("happened some problem, a reader was not added");
        }
    }

    /**
     * Edit a profile of reader
     * @param readerDto - the reader who edits a profile
     * @return true if the profile is edited, false if reader is null or reader is not exist or not active
     */
    @Override
    public boolean editProfile(ReaderDto readerDto) {
        LOGGER.info("editProfile");
        LOGGER.debug("readerDto={}", readerDto);
        if(readerDto == null || !readerDao.exist(readerDto.getReaderId())){
            LOGGER.info("reader is not edited, because a request reader is null or not exist or not active");
            return false;
        }
        IReader reader = ReaderMapper.getReader(readerDto);
        if(readerDao.update(reader) == 1){
            LOGGER.info("reader is edited");
            return true;
        }
        LOGGER.warn("happened some problem, a reader is not edited");
        return false;
    }

    /**
     * remove a reader (edit active to false)
     * @param id reader id
     * @return true if the reader has active false, false if id is null or the reader don`t edit active to false
     */
    @Override
    public boolean removeProfile(Integer id) {
        LOGGER.info("removeProfile");
        LOGGER.debug("readerId={}", id);
        if(id == null){
            LOGGER.warn("reader id is null");
            return false;
        }
        Optional<IReader> readerOpt = readerDao.findReaderById(id);
        if(readerOpt.isEmpty()){
            LOGGER.info("reader is not found");
            return false;
        }
        IReader reader = readerOpt.get();
        if(!reader.getActive()){
            LOGGER.info("the reader has active = false");
            return false;
        }
        removeBooks(reader);
        int result = readerDao.delete(reader);
        if(result == 1){
            LOGGER.info("the reader(id={}) was removed", id);
        } else if(result > 1){
            LOGGER.warn("result={} > 1 from readerDao.delete", result);
        } else {
            LOGGER.info("the reader(id={}) was not removed", id);
            return false;
        }
        if(!readerDao.exist(id)){
            return true;
        }
        return false;
    }

    private void removeBooks(IReader reader) {
        if(bookDao.existReader(reader.getReaderId())){
            LOGGER.info("bookDao.existReader = true");
            if(bookDao.removeReaderFromBooks(reader.getReaderId()) >= 1){
                LOGGER.info("the reader was removed from books");
            } else {
                LOGGER.info("the reader was not removed from books");
            }
        } else {
            LOGGER.info("bookDao.existReader = false");
        }
    }

    @Override
    public boolean restoreProfile(Integer id) {
        LOGGER.info("restoreProfile");
        LOGGER.debug("readerId={}", id);
        if(id == null){
            LOGGER.warn("reader id is null");
            return false;
        }
        Optional<IReader> readerOpt = readerDao.findReaderById(id);
        if(readerOpt.isEmpty()){
            LOGGER.info("reader is not found");
            return false;
        }
        IReader reader = readerOpt.get();
        if(reader.getActive()){
            LOGGER.info("the reader is already restored");
            return false;
        }
        int result = readerDao.restore(reader);
        if(result == 1){
            LOGGER.info("the reader(id={}) was restored", id);
        } else if(result > 1){
            LOGGER.warn("result={} > 1 from readerDao.restore", result);
        } else {
            LOGGER.info("the reader(id={}) was not restored", id);
            return false;
        }
        if(readerDao.exist(id)){
            return true;
        }
        return false;
    }
}
