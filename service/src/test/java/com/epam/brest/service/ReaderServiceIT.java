package com.epam.brest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.brest.dao.ReaderDao;
import com.epam.brest.dao.jdbc.BookDaoSpringJdbc;
import com.epam.brest.dao.jdbc.ReaderDaoSpringJdbc;
import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.sample.SearchReaderSample;
import com.epam.brest.testdb.SpringTestConfig;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
  public void shouldFindProfileByIdWithBooks() {
    Integer readerId = 1;
    ReaderSample reader = readerService.findReaderById(readerId);
    assertNotNull(reader);
    assertEquals(readerId, reader.getReaderId());
    assertNotNull(reader.getBooks());
    assertFalse(reader.getBooks().isEmpty());
    reader.getBooks().forEach(Assertions::assertNotNull);
  }

  @Test
  public void shouldFindProfileByIdWithEmptyListOfBooks() {
    ReaderSample readerSample = new ReaderSample("test", "test", "test");
    readerSample = readerService.createReader(readerSample);
    assertNotNull(readerSample.getReaderId());

    ReaderSample reader = readerService.findReaderById(readerSample.getReaderId());
    assertNotNull(reader);
    assertEquals(readerSample.getReaderId(), reader.getReaderId());
    assertNotNull(reader.getBooks());
    assertTrue(reader.getBooks().isEmpty());
  }

  @Test
  public void shouldReturnNullWhenProfileNotFound() {
    Integer readerId = 999999;
    ReaderSample reader = readerService.findReaderById(readerId);
    assertNull(reader);
  }

  @Test
  public void shouldFindProfileByIdWithoutBooks() {
    Integer readerId = 1;
    ReaderSample reader = readerService.findReaderByIdWithoutBooks(readerId);
    assertNotNull(reader);
    assertEquals(readerId, reader.getReaderId());
    assertNull(reader.getBooks());
  }

  @Test
  public void shouldReturnNullWhenProfileNotFoundByIdWithoutBooks() {
    Integer readerId = 9999999;
    ReaderSample reader = readerService.findReaderByIdWithoutBooks(readerId);
    assertNull(reader);
  }

  @Test
  public void shoundReturnReaderSampleWithIdAfterCreatingReader() {
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
  public void shouldReturnTrueAfterEditingReader() {
    Integer readerId = 1;
    ReaderSample reader = readerService.findReaderByIdWithoutBooks(readerId);
    assertEquals(readerId, reader.getReaderId());
    reader.setFirstName("test");
    reader.setLastName("test");
    assertTrue(readerService.editReader(reader));

    reader = readerService.findReaderByIdWithoutBooks(readerId);
    assertEquals("test", reader.getFirstName());
    assertEquals("test", reader.getLastName());
  }

  @Test
  public void shouldReturnFalseWhenRequestReaderIsNotExistBeforeEditingReader() {
    ReaderSample readerSample = new ReaderSample();
    readerSample.setReaderId(9999999);
    assertFalse(readerService.editReader(readerSample));
  }

  @Test
  public void shouldReturnTrueAfterDeletingReader() {
    assertTrue(readerService.changeReaderToNoActive(1));
    ReaderSample reader = readerService.findReaderById(1);
    assertNotNull(reader);
    assertTrue(reader.getBooks().isEmpty());
    assertTrue(readerDao.isExistAmongReadersByActive(1, false));
  }

  @Test
  public void shouldReturnFalseWhenIdIsNullBeforeDeletingReader() {
    assertFalse(readerService.changeReaderToNoActive(null));
  }

  @Test
  public void shouldReturnFalseWhenReaderIsNotFoundBeforeDeletingReader() {
    assertFalse(readerService.changeReaderToNoActive(9999999));
  }

  @Test
  public void shouldReturnFalseWhenReaderIsRemovedBeforeDeletingReader() {
    assertTrue(readerService.changeReaderToNoActive(1));
    ReaderSample reader = readerService.findReaderById(1);
    assertNotNull(reader);
    assertTrue(reader.getBooks().isEmpty());
    assertTrue(readerDao.isExistAmongReadersByActive(1, false));

    assertFalse(readerService.changeReaderToNoActive(1));
  }

  @Test
  public void shouldReturnTrueAfterRestoringReader() {
    assertTrue(readerService.changeReaderToNoActive(1));
    ReaderSample reader = readerService.findReaderById(1);
    assertNotNull(reader);
    assertTrue(reader.getBooks().isEmpty());
    assertTrue(readerDao.isExistAmongReadersByActive(1, false));

    assertTrue(readerService.changeReaderToActive(1));
    assertTrue(readerDao.isExistAmongReadersByActive(1, true));
  }

  @Test
  public void shouldReturnFalseWhenIdIsNullBeforeRestoringReader() {
    assertFalse(readerService.changeReaderToActive(null));
  }

  @Test
  public void shouldReturnFalseWhenReaderIsNotFoundBeforeRestoringReader() {
    assertFalse(readerService.changeReaderToActive(9999999));
  }

  @Test
  public void shouldReturnFalseWhenReaderIsNotRemovedBeforeRestoringReader() {
    assertFalse(readerService.changeReaderToActive(1));
  }

  @Test
  public void shouldReturnAllReaders() {
    List<ReaderSample> readers = readerService.findAll();
    assertFalse(readers.isEmpty());
  }

  @Test
  public void shouldReturnAllFoundReaders() {
    SearchReaderSample searchReaderSample = new SearchReaderSample();
    searchReaderSample.setFrom(LocalDate.of(2020, 01, 13));
    searchReaderSample.setTo(LocalDate.now());
    List<ReaderSample> readers = readerService.searchReaders(searchReaderSample);
    assertFalse(readers.isEmpty());
    readers.forEach(r -> {
      assertTrue(searchReaderSample.getFrom().isBefore(r.getDateOfRegistry()));
      assertTrue(searchReaderSample.getTo().isAfter(r.getDateOfRegistry()));
    });
  }

}
