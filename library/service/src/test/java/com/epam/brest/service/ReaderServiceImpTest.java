package com.epam.brest.service;

import com.epam.brest.dao.ReaderDao;
import com.epam.brest.model.Reader;
import com.epam.brest.model.sample.ReaderSample;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:service-context.xml", "classpath*:test-db-h2.xml", "classpath*:springContextJdbc.xml"})
public class ReaderServiceImpTest {

    @Autowired
    private ReaderServiceImp readerService;
    @Autowired
    private ReaderDao readerDao;

    /**
     * getProfile with books
     */
    @Test
    public void getProfileTest() {
        ReaderSample readerSample = readerService.getProfile(1);
        Assert.assertNotNull(readerSample);
        Assert.assertEquals(Integer.valueOf(1), readerSample.getReaderId());
    }

    /**
     * getProfileWithoutBooks
     */
    @Test
    public void getProfileWithoutBooksTest(){
        ReaderSample readerSample = readerService.getProfileWithoutBooks(1);
        Assert.assertNotNull(readerSample);
        Assert.assertEquals(Integer.valueOf(1), readerSample.getReaderId());
    }

    /**
     * createReader
     * all done correctly
     */
    @Test
    public void createReaderTest() {
        ReaderSample readerSample = new ReaderSample();
        readerSample.setLastName("last");
        readerSample.setFirstName("first");
        readerSample.setPatronymic("patr");

        ReaderSample result = readerService.createReader(readerSample);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getReaderId());
        Assert.assertEquals(readerSample.getFirstName(), result.getFirstName());
        Assert.assertEquals(readerSample.getLastName(), result.getLastName());
        Assert.assertEquals(readerSample.getPatronymic(), result.getPatronymic());
    }

    /**
     * editProfile
     * All done correctly
     */
    @Test
    public void editProfileTest() {
        ReaderSample readerSample = readerService.getProfile(1);
        Assert.assertNotNull(readerSample);
        Assert.assertEquals(Integer.valueOf(1), readerSample.getReaderId());

        readerSample.setFirstName("first");
        readerSample.setPatronymic("patron");
        boolean result = readerService.editProfile(readerSample);
        Assert.assertTrue(result);

        ReaderSample updatedReader = readerService.getProfile(1);
        Assert.assertNotNull(updatedReader);
        Assert.assertEquals(Integer.valueOf(1), updatedReader.getReaderId());
        Assert.assertEquals(readerSample, updatedReader);
    }

    /**
     * editProfile
     * ReaderDto is null
     */
    @Test
    public void editProfileWithReaderDtoNullTest(){
        ReaderSample readerSample = null;
        boolean result = readerService.editProfile(readerSample);
        Assert.assertFalse(result);
    }
    /**
     * editProfile
     * Reader is not exist in DB
     */
    @Test
    public void editProfileWhenReaderNotExistTest(){
        ReaderSample readerSample = new ReaderSample();
        readerSample.setReaderId(99);
        boolean result = readerService.editProfile(readerSample);
        Assert.assertFalse(result);
    }

    /**
     * remove profile
     */
    @Test
    public void removeProfileTest() {
        //all done correctly
        boolean result = readerService.removeProfile(1);
        Assert.assertTrue(result);
        Optional<Reader> readerOptional = readerDao.findReaderByIdWithBooks(1);
        if(readerOptional.isPresent()){
            Reader reader = readerOptional.get();
            Assert.assertEquals(1, (int) reader.getReaderId());
            Assert.assertFalse(reader.getActive());
            Assert.assertTrue(reader.getBooks().isEmpty());
        }
        //reader is already removed
        result = readerService.removeProfile(1);
        Assert.assertFalse(result);

        //id is null
        result = readerService.removeProfile(null);
        Assert.assertFalse(result);

        //reader not found
        result = readerService.removeProfile(999);
        Assert.assertFalse(result);
    }
    /**
     * restore profile
     */
    @Test
    public void restoreProfileTest(){
        boolean result;
        Optional<Reader> readerOptional = readerDao.findReaderByIdWithBooks(1);
        if(readerOptional.isPresent()) {
            Reader reader = readerOptional.get();
            if(reader.getActive()){
                result = readerService.removeProfile(1);
                Assert.assertTrue(result);
            }
        }

        //all done correctly
        result = readerService.restoreProfile(1);
        Assert.assertTrue(result);
        readerOptional = readerDao.findReaderByIdWithBooks(1);
        if(readerOptional.isPresent()){
            Reader reader = readerOptional.get();
            Assert.assertNotNull(reader);
            Assert.assertEquals(1, (int) reader.getReaderId());
            Assert.assertTrue(reader.getActive());
            Assert.assertTrue(reader.getBooks().isEmpty());
        }

        //reader is already restored
        result = readerService.restoreProfile(1);
        Assert.assertFalse(result);

        //id is null
        result = readerService.removeProfile(null);
        Assert.assertFalse(result);

        //reader not found
        result = readerService.removeProfile(999);
        Assert.assertFalse(result);
    }



}
