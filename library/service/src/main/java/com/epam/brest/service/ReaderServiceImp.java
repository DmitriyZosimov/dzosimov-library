package com.epam.brest.service;

import com.epam.brest.dao.BookDao;
import com.epam.brest.dao.ReaderDao;
import com.epam.brest.model.Reader;
import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.tools.ReaderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service("readerService")
@Transactional
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
     * @return reader if exist or null;
     */
    @Override
    public ReaderSample getProfile(Integer id) {
        LOGGER.info("getProfile");
        Optional<Reader> readerOpt = readerDao.findReaderByIdWithBooks(id);
        if(readerOpt.isEmpty()){
            return null;
        }
        return ReaderMapper.getReaderSample(readerOpt.get());
    }

    /**
     * Find a profile of a reader by id
     * @param id - reader id
     * @return reader if exist or null
     */
    @Override
    public ReaderSample getProfileWithoutBooks(Integer id) {
        LOGGER.info("getProfileWithoutBooks");
        Optional<Reader> readerOpt = readerDao.findReaderById(id);
        if(readerOpt.isEmpty()){
            return null;
        }
        return ReaderMapper.getReaderSample(readerOpt.get());
    }

    /**
     * Add a new reader
     * @param readerSample reader
     * @return saved ReaderSample or null
     */
    @Override
    public ReaderSample createReader(ReaderSample readerSample) {
        LOGGER.info("createReader");
        LOGGER.debug("ReaderSample={}", readerSample);
        if (readerSample == null){
            LOGGER.info("reader is not added, because a request reader is null");
            return null;
        }
        Reader reader = ReaderMapper.getReader(readerSample);
        reader.setDateOfRegistry(LocalDate.now());
        reader = readerDao.save(reader);
        if(reader.getReaderId() != null){
            LOGGER.info("reader was added");
            return ReaderMapper.getReaderSample(reader);
        } else {
            LOGGER.warn("happened some problem, a reader was not added");
            return null;
        }
    }

    /**
     * Edit a profile of reader
     * @param readerSample - the reader who edits a profile
     * @return true if the profile is edited, false if reader is null or reader is not exist or not active
     */
    @Override
    public Boolean editProfile(ReaderSample readerSample) {
        LOGGER.info("editProfile");
        LOGGER.debug("readerSample={}", readerSample);
        if(readerSample == null || !readerDao.exist(readerSample.getReaderId())){
            LOGGER.info("reader is not edited, because a request reader is null or not exist or not active");
            return false;
        }
        Reader reader = ReaderMapper.getReader(readerSample);
        int result = readerDao.update(reader);
        return checkResult(reader.getReaderId(), result);
    }

    /**
     * remove a reader (edit active to false)
     * @param id reader id
     * @return true if the reader has active false, false if id is null or the reader don`t edit active to false
     */
    @Override
    public Boolean removeProfile(Integer id) {
        LOGGER.info("removeProfile");
        LOGGER.debug("readerId={}", id);
        if(id == null){
            LOGGER.warn("reader id is null");
            return false;
        }
        Optional<Reader> readerOpt = readerDao.findReaderById(id);
        if(readerOpt.isEmpty()){
            LOGGER.info("reader is not found");
            return false;
        }
        Reader reader = readerOpt.get();
        if(!reader.getActive()){
            LOGGER.info("the reader has active = false");
            return false;
        }
        bookDao.removeReaderFromBooks(reader.getReaderId());
        int result = readerDao.delete(reader);
        checkResult(reader.getReaderId(), result);
        if(!readerDao.exist(id)){
            return true;
        }
        return false;
    }

    /**
     * restore a reader (edit active to true)
     * @param id reader id
     * @return true if the reader has active true, false if id is null or the reader don`t edit active to true
     */
    @Override
    public Boolean restoreProfile(Integer id) {
        LOGGER.info("restoreProfile");
        LOGGER.debug("readerId={}", id);
        if(id == null){
            LOGGER.warn("reader id is null");
            return false;
        }
        Optional<Reader> readerOpt = readerDao.findReaderById(id);
        if(readerOpt.isEmpty()){
            LOGGER.info("reader is not found");
            return false;
        }
        Reader reader = readerOpt.get();
        if(reader.getActive()){
            LOGGER.info("the reader is already restored");
            return false;
        }
        int result = readerDao.restore(reader);
        checkResult(id, result);
        if(readerDao.exist(id)){
            return true;
        }
        return false;
    }

    //TODO:maybe not needed
    private Boolean checkResult(Integer readerId, int result) {
        if (result == 1) {
            LOGGER.info("The reader(readerId={}) - all done correctly", readerId);
            return true;
        } else if (result > 1) {
            LOGGER.warn("result={} > 1", result);
            return true;
        } else {
            LOGGER.info("The reader(readerId={}) - some done wrong", readerId);
            return false;
        }
    }
}
