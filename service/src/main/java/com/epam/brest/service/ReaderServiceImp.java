package com.epam.brest.service;

import com.epam.brest.dao.BookDao;
import com.epam.brest.dao.ReaderDao;
import com.epam.brest.model.Reader;
import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.sample.SearchReaderSample;
import com.epam.brest.model.tools.ReaderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service("readerService")
@Transactional
public class ReaderServiceImp implements ReaderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderServiceImp.class);

    private final ReaderDao readerDao;
    private final BookDao bookDao;

    @Autowired
    public ReaderServiceImp(ReaderDao readerDao, BookDao bookDao) {
        this.readerDao = readerDao;
        this.bookDao = bookDao;
    }

    @Transactional(readOnly = true)
    @Override
    public ReaderSample getProfile(Integer id) {
        LOGGER.info("getProfile");
        Optional<Reader> readerOpt = readerDao.findReaderByIdWithBooks(id);
        if(readerOpt.isEmpty()){
            return null;
        }
        return ReaderMapper.getReaderSample(readerOpt.get());
    }

    @Transactional(readOnly = true)
    @Override
    public ReaderSample getProfileWithoutBooks(Integer id) {
        LOGGER.info("getProfileWithoutBooks");
        Optional<Reader> readerOpt = readerDao.findReaderById(id);
        if(readerOpt.isEmpty()){
            return null;
        }
        return ReaderMapper.getReaderSample(readerOpt.get());
    }

    @Override
    public ReaderSample createReader(ReaderSample readerSample) {
        LOGGER.info("createReader");
        LOGGER.debug("ReaderSample={}", readerSample);
        Reader reader = ReaderMapper.getReader(readerSample);
        reader.setDateOfRegistry(LocalDate.now());
        reader = readerDao.save(reader);
        return ReaderMapper.getReaderSample(reader);

    }

    @Override
    public Boolean editProfile(ReaderSample readerSample) {
        LOGGER.info("editProfile");
        LOGGER.debug("readerSample={}", readerSample);
        Reader reader = ReaderMapper.getReader(readerSample);
        int result = readerDao.update(reader);
        return checkResult(reader.getReaderId(), result);
    }

    @Override
    public Boolean removeProfile(Integer id) {
        LOGGER.info("removeProfile");
        LOGGER.debug("readerId={}", id);
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

    @Override
    public Boolean restoreProfile(Integer id) {
        LOGGER.info("restoreProfile");
        LOGGER.debug("readerId={}", id);
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

    @Transactional(readOnly = true)
    @Override
    public List<ReaderSample> findAll() {
        List<ReaderSample> readers = new LinkedList<>();
        readerDao.findAll().forEach(r -> {
            readers.add(ReaderMapper.getReaderSample(r));
        });
        return readers;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReaderSample> searchReaders(SearchReaderSample searchReaderSample) {
        List<ReaderSample> readers = new LinkedList<>();
        readerDao.findAllByDate(searchReaderSample.getFrom(), searchReaderSample.getTo())
                .forEach(r -> {
                    readers.add(ReaderMapper.getReaderSample(r));
                });
        return readers;
    }

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
