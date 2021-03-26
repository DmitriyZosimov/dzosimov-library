package com.epam.brest.dao.jdbc;

import com.epam.brest.model.IReader;
import com.epam.brest.model.Reader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-db-h2.xml", "classpath*:springContextJdbc.xml",
        "classpath*:dao.xml"})
public class ReaderDaoSpringJdbcTest {

    @Autowired
    private ReaderDaoSpringJdbc dao;

    @Test
    public void testFindAll(){
        List<IReader> readers = dao.findAll();
        Assert.assertNotNull(readers);
        Assert.assertTrue(readers.size() > 0);
    }

    @Test
    public void testFindAllActive() {
        List<IReader> readers = dao.findAllActive();
        int size = readers.size();
        Assert.assertNotNull(readers);
        Assert.assertTrue(readers.size() > 0);
        readers.stream().forEach(r -> Assert.assertTrue(r.getActive()));
    }

    @Test
    public void testFindReaderById() {
        List<IReader> readers = dao.findAll();
        Assert.assertNotNull(readers);
        Assert.assertTrue(readers.size() > 0);

        IReader reader = dao.findReaderById(readers.get(0).getReaderId()).get();
        Assert.assertNotNull(reader);
        Assert.assertEquals(reader, readers.get(0));
    }

    @Test
    public void testFindReaderByIdWithBooks() {
        List<IReader> readers = dao.findAll();
        Assert.assertNotNull(readers);
        Assert.assertTrue(readers.size() > 0);

        IReader reader = dao.findReaderByIdWithBooks(readers.get(0).getReaderId()).get();
        Assert.assertNotNull(reader);
        Assert.assertTrue(reader.getReaderId() == readers.get(0).getReaderId());
        Assert.assertNotNull(reader.getBooks());
        reader.getBooks().stream().forEach(b -> {
            Assert.assertNotNull(b);
            Assert.assertEquals(reader.getReaderId(), b.getReader().getReaderId());});
    }

    @Test
    public void testSave() {
        IReader reader = new Reader("Zosimov", "Dima", "Alex", LocalDate.now());
        reader = dao.save(reader);
        Assert.assertNotNull(reader.getReaderId());
        Assert.assertTrue(reader.getReaderId() > 0);
    }

    @Test
    public void testUpdate() {
        List<IReader> readers = dao.findAll();
        Assert.assertNotNull(readers);
        Assert.assertTrue(readers.size() > 0);

        IReader reader = readers.get(0);
        reader.setFirstName("NewFirstName");
        reader.setLastName("NewLastName");
        reader.setPatronymic("NewPatronymic");
        reader.setDateOfRegistry(LocalDate.now());

        int res = dao.update(reader);
        Assert.assertTrue(res == 1);
    }

    @Test
    public void testDelete() {
        List<IReader> readers = dao.findAll();
        Assert.assertNotNull(readers);
        Assert.assertTrue(readers.size() > 0);

        IReader reader = readers.get(0);

        int res = dao.delete(reader);
        Assert.assertTrue(res == 1);
        Assert.assertFalse(dao.findReaderById(reader.getReaderId()).get().getActive());
    }

    @Test
    public void testRestore(){
        List<IReader> readers = dao.findAll();
        Assert.assertNotNull(readers);
        Assert.assertTrue(readers.size() > 0);
        IReader reader = readers.get(0);
        int res = dao.delete(reader);
        Assert.assertTrue(res == 1);

        res = dao.restore(reader);
        Assert.assertTrue(res == 1);
        Assert.assertTrue(dao.findReaderById(reader.getReaderId()).get().getActive());
    }

    @Test
    public void testExist() {
        List<IReader> readers = dao.findAll();
        Assert.assertNotNull(readers);
        Assert.assertTrue(readers.size() > 0);

        Assert.assertTrue(dao.exist(readers.get(0).getReaderId()));
    }
}