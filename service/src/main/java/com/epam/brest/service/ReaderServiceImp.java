package com.epam.brest.service;

import com.epam.brest.dao.BookDao;
import com.epam.brest.dao.ReaderDao;
import com.epam.brest.model.Reader;
import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.sample.SearchReaderSample;
import com.epam.brest.model.tools.ReaderMapper;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public ReaderSample findReaderById(Integer id) {
    LOGGER.info("getProfile");
    Optional<Reader> readerOpt = readerDao.findReaderByIdWithBooks(id);
    return (readerOpt.isEmpty()) ? null : ReaderMapper.getReaderSample(readerOpt.get());
  }

  @Transactional(readOnly = true)
  @Override
  public ReaderSample findReaderByIdWithoutBooks(Integer id) {
    LOGGER.info("getProfileWithoutBooks");
    Optional<Reader> readerOpt = readerDao.findReaderById(id);
    return (readerOpt.isEmpty()) ? null : ReaderMapper.getReaderSample(readerOpt.get());
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
  public Boolean editReader(ReaderSample readerSample) {
    LOGGER.info("editReader");
    LOGGER.debug("readerSample={}", readerSample);
    Reader reader = ReaderMapper.getReader(readerSample);
    int result = readerDao.update(reader);
    return result > 0;
  }

  @Override
  public Boolean changeReaderToNoActive(Integer id) {
    LOGGER.info("changeReaderToNoActive");
    LOGGER.debug("readerId={}", id);
    Optional<Reader> readerOpt = readerDao.findReaderById(id);
    if (readerOpt.isEmpty()) {
      LOGGER.info("reader not found");
      return false;
    }
    Reader reader = readerOpt.get();
    if (!reader.isActive()) {
      LOGGER.info("the reader has active = false");
      return false;
    }
    bookDao.removeAllBooksFromReader(reader.getReaderId());
    readerDao.changeReaderToNoActive(reader);
    return !readerDao.isExistAmongActiveReaders(id);
  }

  @Override
  public Boolean changeReaderToActive(Integer id) {
    LOGGER.info("changeReaderToActive");
    LOGGER.debug("readerId={}", id);
    Optional<Reader> readerOpt = readerDao.findReaderById(id);
    if (readerOpt.isEmpty()) {
      LOGGER.info("reader is not found");
      return false;
    }
    Reader reader = readerOpt.get();
    if (reader.isActive()) {
      LOGGER.info("the reader is already restored");
      return false;
    }
    readerDao.changeReaderToActive(reader);
    return readerDao.isExistAmongActiveReaders(id);
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
}
