package com.epam.brest.dao;

import com.epam.brest.model.Reader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:test-db.xml"})
public class TestReaderDao {
    @Autowired
    private ReaderDao readerDao;

    @Test
    public void testFindAllReader(){
        List<Reader> readers = readerDao.findAll();
        Assert.assertTrue(readers.size() > 0);
    }

    @Test
    public void testInsertNewReader(){
        Reader reader = new Reader("Dima", "Zosimov", "Alex", LocalDate.now());
        reader = readerDao.save(reader);
        Assert.assertNotNull(reader.getReaderId());
    }

    @Test
    public void testUpdateReader(){
        Reader reader = readerDao.findById(1).get();
        LocalDate oldDate = reader.getDateOfRegistry();
        reader.setDateOfRegistry(LocalDate.of(2020, (int)(Math.random() * 12), (int)(Math.random() * 28)));
        readerDao.save(reader);
        Assert.assertNotEquals(oldDate, reader.getDateOfRegistry());
    }

    @Test
    public void testFindAllActiveReaders(){
        Reader passiveReader = new Reader("", "", "", LocalDate.now(), Boolean.FALSE);
        readerDao.save(passiveReader);
        List<Reader> activeReaders = readerDao.findAllActiveReaders();
        List<Reader> allReaders = readerDao.findAll();
        allReaders.removeAll(activeReaders);
        allReaders.stream().forEach(r -> Assert.assertFalse(r.getActive()));
    }

    @Test
    public void testRemoveReader(){
        Reader reader = readerDao.findById(1).get();
        int result = readerDao.removeReader(1);
        Assert.assertTrue(result == 1);
        reader = readerDao.findById(1).get();
        Assert.assertFalse(reader.getActive());
    }
}
