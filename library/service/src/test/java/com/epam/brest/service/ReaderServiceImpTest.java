package com.epam.brest.service;

import com.epam.brest.dao.ReaderDao;
import com.epam.brest.model.IReader;
import com.epam.brest.model.dto.ReaderDto;
import com.epam.brest.model.dto.ReaderDtoWithBooks;
import com.epam.brest.service.exception.ReaderCreationException;
import com.epam.brest.service.exception.ReaderNotFoundException;
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
    public void getProfileTest() throws ReaderNotFoundException {
        ReaderDto readerDto = readerService.getProfile(1);
        Assert.assertNotNull(readerDto);
        Assert.assertEquals(Integer.valueOf(1), readerDto.getReaderId());
        Assert.assertTrue(ReaderDtoWithBooks.class.isInstance(readerDto));
    }
    /**
     * getProfile with books
     * execute ReaderNotFoundException
     */
    @Test(expected = ReaderNotFoundException.class)
    public void getProfileReaderNotFoundExceptionTest() throws ReaderNotFoundException {
        ReaderDto readerDto = readerService.getProfile(99);
    }

    /**
     * getProfileWithoutBooks
     */
    @Test
    public void getProfileWithoutBooksTest() throws ReaderNotFoundException {
        ReaderDto readerDto = readerService.getProfileWithoutBooks(1);
        Assert.assertNotNull(readerDto);
        Assert.assertEquals(Integer.valueOf(1), readerDto.getReaderId());
    }
    /**
     * getProfileWithoutBooks
     * execute ReaderNotFoundException
     */
    @Test(expected = ReaderNotFoundException.class)
    public void getProfileWithoutBooksReaderNotFoundExceptionTest() throws ReaderNotFoundException {
        ReaderDto readerDto = readerService.getProfileWithoutBooks(99);
    }

    /**
     * createReader
     * all done correctly
     */
    @Test
    public void createReaderTest() throws ReaderCreationException {
        ReaderDto readerDto = new ReaderDto();
        readerDto.setLastName("last");
        readerDto.setFirstName("first");
        readerDto.setPatronymic("patr");

        ReaderDto result = readerService.createReader(readerDto);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getReaderId());
        Assert.assertEquals(readerDto.getFirstName(), result.getFirstName());
        Assert.assertEquals(readerDto.getLastName(), result.getLastName());
        Assert.assertEquals(readerDto.getPatronymic(), result.getPatronymic());
    }

    /**
     * createReader
     * ReaderDto is null
     * execute ReaderCreationException
     */
    @Test(expected = ReaderCreationException.class)
    public void createReaderWithExceptionTest() throws ReaderCreationException {
        ReaderDto result = readerService.createReader(null);
    }

    /**
     * editProfile
     * All done correctly
     */
    @Test
    public void editProfileTest() throws ReaderNotFoundException {
        ReaderDto readerDto = readerService.getProfile(1);
        Assert.assertNotNull(readerDto);
        Assert.assertEquals(Integer.valueOf(1), readerDto.getReaderId());

        readerDto.setFirstName("first");
        readerDto.setPatronymic("patron");
        boolean result = readerService.editProfile(readerDto);
        Assert.assertTrue(result);

        ReaderDto updatedReader = readerService.getProfile(1);
        Assert.assertNotNull(updatedReader);
        Assert.assertEquals(Integer.valueOf(1), updatedReader.getReaderId());
        Assert.assertEquals(readerDto, updatedReader);
    }

    /**
     * editProfile
     * ReaderDto is null
     */
    @Test
    public void editProfileWithReaderDtoNullTest(){
        ReaderDto readerDto = null;
        boolean result = readerService.editProfile(readerDto);
        Assert.assertFalse(result);
    }
    /**
     * editProfile
     * Reader is not exist in DB
     */
    @Test
    public void editProfileWhenReaderNotExistTest(){
        ReaderDto readerDto = new ReaderDto();
        readerDto.setReaderId(99);
        boolean result = readerService.editProfile(readerDto);
        Assert.assertFalse(result);
    }

    /**
     * remove profile
     */
    @Test
    public void removeProfileTest() throws ReaderNotFoundException {
        //all done correctly
        boolean result = readerService.removeProfile(1);
        Assert.assertTrue(result);
        Optional<IReader> readerOptional = readerDao.findReaderByIdWithBooks(1);
        if(readerOptional.isPresent()){
            IReader reader = readerOptional.get();
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
        Optional<IReader> readerOptional = readerDao.findReaderByIdWithBooks(1);
        if(readerOptional.isPresent()) {
            IReader reader = readerOptional.get();
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
            IReader reader = readerOptional.get();
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
