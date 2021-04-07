package com.epam.brest.service;

import com.epam.brest.dao.ReaderDao;
import com.epam.brest.dao.jdbc.BookDaoSpringJdbc;
import com.epam.brest.dao.jdbc.ReaderDaoSpringJdbc;
import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.testdb.SpringTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({ReaderServiceImp.class, ReaderDaoSpringJdbc.class, BookDaoSpringJdbc.class})
@PropertySource({"classpath:dao.properties"})
@ComponentScan({"com.epam.brest.testdb", "com.epam.brest.dao"})
@ContextConfiguration(classes = {SpringTestConfig.class})
@Transactional
public class ReaderServiceIT {

    @Autowired
    private ReaderServiceImp readerService;
    @Autowired
    private ReaderDao readerDao;

    @Test
    public void shouldFindProfileByIdWithBooks(){
        Integer readerId = 1;
        ReaderSample reader = readerService.getProfile(readerId);
        assertNotNull(reader);
        assertEquals(readerId, reader.getReaderId());
        assertNotNull(reader.getBooks());
        assertFalse(reader.getBooks().isEmpty());
        reader.getBooks().forEach(Assertions::assertNotNull);
    }

    @Test
    public void shouldFindProfileByIdWithEmptyListOfBooks(){
        ReaderSample readerSample = new ReaderSample("test", "test", "test");
        readerSample = readerService.createReader(readerSample);
        assertNotNull(readerSample.getReaderId());

        ReaderSample reader = readerService.getProfile(readerSample.getReaderId());
        assertNotNull(reader);
        assertEquals(readerSample.getReaderId(), reader.getReaderId());
        assertNotNull(reader.getBooks());
        assertTrue(reader.getBooks().isEmpty());
    }

    @Test
    public void shouldReturnNullWhenProfileNotFound(){
        Integer readerId = 999999;
        ReaderSample reader = readerService.getProfile(readerId);
        assertNull(reader);
    }

    @Test
    public void shouldFindProfileByIdWithoutBooks(){
        Integer readerId = 1;
        ReaderSample reader = readerService.getProfileWithoutBooks(readerId);
        assertNotNull(reader);
        assertEquals(readerId, reader.getReaderId());
        assertNull(reader.getBooks());
    }

    @Test
    public void shouldReturnNullWhenProfileNotFoundByIdWithoutBooks(){
        Integer readerId = 9999999;
        ReaderSample reader = readerService.getProfileWithoutBooks(readerId);
        assertNull(reader);
    }

    @Test
    public void shoundReturnReaderSampleWithIdAfterCreatingReader(){
        ReaderSample readerSample = new ReaderSample("test", "test", "test");
        readerSample = readerService.createReader(readerSample);
        assertNotNull(readerSample.getReaderId());
        assertEquals("test", readerSample.getFirstName());
        assertEquals("test", readerSample.getLastName());
        assertEquals("test", readerSample.getPatronymic());
        assertNotNull(readerSample.getDateOfRegistry());
        assertNull(readerSample.getBooks());
    }

    @Test
    public void shoundReturnNullWhenRequestReaderSampleIsNullBeforeCreatingReader(){
        ReaderSample readerSample = null;
        readerSample = readerService.createReader(readerSample);
        assertNull(readerSample);
    }

    @Test
    public void shouldReturnTrueAfterEditingReader(){
        Integer readerId = 1;
        ReaderSample reader = readerService.getProfileWithoutBooks(readerId);
        assertEquals(readerId, reader.getReaderId());
        reader.setFirstName("test");
        reader.setLastName("test");
        assertTrue(readerService.editProfile(reader));

        reader = readerService.getProfileWithoutBooks(readerId);
        assertEquals("test", reader.getFirstName());
        assertEquals("test", reader.getLastName());
    }

    @Test
    public void shouldReturnFalseWhenRequestReaderIsNullBeforeEditingReader(){
        assertFalse(readerService.editProfile(null));
    }

    @Test
    public void shouldReturnFalseWhenRequestReaderIsNotExistBeforeEditingReader(){
        ReaderSample readerSample = new ReaderSample();
        readerSample.setReaderId(9999999);
        assertFalse(readerService.editProfile(readerSample));
    }

    @Test
    public void shouldReturnTrueAfterDeletingReader(){
        assertTrue(readerService.removeProfile(1));
        ReaderSample reader = readerService.getProfile(1);
        assertNotNull(reader);
        assertTrue(reader.getBooks().isEmpty());
        assertTrue(readerDao.exist(1, false));
    }

    @Test
    public void shouldReturnFalseWhenIdIsNullBeforeDeletingReader(){
        assertFalse(readerService.removeProfile(null));
    }

    @Test
    public void shouldReturnFalseWhenReaderIsNotFoundBeforeDeletingReader(){
        assertFalse(readerService.removeProfile(9999999));
    }

    @Test
    public void shouldReturnFalseWhenReaderIsRemovedBeforeDeletingReader(){
        assertTrue(readerService.removeProfile(1));
        ReaderSample reader = readerService.getProfile(1);
        assertNotNull(reader);
        assertTrue(reader.getBooks().isEmpty());
        assertTrue(readerDao.exist(1, false));

        assertFalse(readerService.removeProfile(1));
    }

    @Test
    public void shouldReturnTrueAfterRestoringReader(){
        assertTrue(readerService.removeProfile(1));
        ReaderSample reader = readerService.getProfile(1);
        assertNotNull(reader);
        assertTrue(reader.getBooks().isEmpty());
        assertTrue(readerDao.exist(1, false));

        assertTrue(readerService.restoreProfile(1));
        assertTrue(readerDao.exist(1, true));
    }

    @Test
    public void shouldReturnFalseWhenIdIsNullBeforeRestoringReader(){
        assertFalse(readerService.restoreProfile(null));
    }

    @Test
    public void shouldReturnFalseWhenReaderIsNotFoundBeforeRestoringReader(){
        assertFalse(readerService.restoreProfile(9999999));
    }

    @Test
    public void shouldReturnFalseWhenReaderIsNotRemovedBeforeRestoringReader(){
        assertFalse(readerService.restoreProfile(1));
    }

}
