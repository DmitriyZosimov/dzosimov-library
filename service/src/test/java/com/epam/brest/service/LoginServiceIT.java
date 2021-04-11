package com.epam.brest.service;

import com.epam.brest.dao.jdbc.ReaderDaoSpringJdbc;
import com.epam.brest.testdb.SpringTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ComponentScan({"com.epam.brest.testdb", "com.epam.brest.dao"})
@Import({LoginServiceImp.class, ReaderDaoSpringJdbc.class, ReaderServiceImp.class})
@ContextConfiguration(classes = {SpringTestConfig.class})
@PropertySource({"classpath:dao.properties"})
@Transactional
public class LoginServiceIT {

    @Autowired
    private ILoginService loginService;
    @Autowired
    private IReaderService readerService;

    @Test
    public void shouldReturnTrueWhenExistReader(){
        assertTrue(loginService.isExistCard(1));
    }

    @Test
    public void shouldReturnFalseWhenIdIsNullBeforeExistingReader(){
        assertFalse(loginService.isExistCard(null));
    }

    @Test
    public void shouldReturnFalseWhenIdNotExist(){
        assertFalse(loginService.isExistCard(999999999));
    }

    @Test
    public void shouldReturnTrueWhenReaderIsRemoved(){
        assertTrue(readerService.removeProfile(1));
        assertTrue(loginService.isRemovedCard(1));
    }

    @Test
    public void shouldReturnFalseWhenIdIsNullBeforeCheckIfReaderIsRemoved(){
        assertFalse(loginService.isRemovedCard(null));
    }

    @Test
    public void shouldReturnFalseWhenIdNotRemoved(){
        assertFalse(loginService.isRemovedCard(2));
    }

    @Test
    public void shouldReturnFalseWhenIdNotExistAfterCheckIfReaderIsRemoved(){
        assertFalse(loginService.isRemovedCard(99999999));
    }
}
