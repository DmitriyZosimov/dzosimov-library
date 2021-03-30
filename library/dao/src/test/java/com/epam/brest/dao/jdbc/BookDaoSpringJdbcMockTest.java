package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.jdbc.tools.CatalogOfBooksMapper;
import com.epam.brest.model.Book;
import com.epam.brest.model.Genre;
import com.epam.brest.model.sample.BookSample;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class BookDaoSpringJdbcMockTest {

    @InjectMocks
    private BookDaoSpringJdbc bookDaoSpringJdbc;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Captor
    private ArgumentCaptor<RowMapper<BookSample>> captorMapper;
    @Captor
    private ArgumentCaptor<MapSqlParameterSource> captorMapSqlPS;
    @Captor
    private ArgumentCaptor<KeyHolder> captorKeyHolder;

    @Test
    public void findAllTest(){

    }

//    @Test
//    public void findBookByIdTest(){
//        String sql = "sql";
//        ReflectionTestUtils.setField(bookDaoSpringJdbc, "findBookByIdSql", sql);
//
//        Book BookDto = new Book();
//        Optional<Book> opt = Optional.of(BookDto);
//
//        Mockito.when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class),
//                any(RowMapper.class))).thenReturn(BookDto);
//
//        Optional<Book> result = bookDaoSpringJdbc.findBookById(anyInt());
//
//        Assert.assertNotNull(result);
//        Assert.assertSame(opt, result);
//
////        Mockito.verify(namedParameterJdbcTemplate).queryForObject(eq(sql), any(SqlParameterSource.class),
////                captorMapper.capture());
////        RowMapper<Book> mapper = captorMapper.getValue();
////        Assert.assertNotNull(mapper);
////        MapSqlParameterSource mapSqlParameterSource = captorMapSqlPS.getValue();
////        Assert.assertNotNull(mapSqlParameterSource);
////
////        Mockito.verifyNoMoreInteractions(namedParameterJdbcTemplate, Mockito.times(1));
//    }
//
//    @Test
//    public void saveTest(){
//
//        Book BookDto = new Book();
//        Mockito.when(namedParameterJdbcTemplate.update(any(), any(SqlParameterSource.class),
//                any(KeyHolder.class))).thenReturn(1);
//        Mockito.when(captorKeyHolder.capture().getKey()).thenReturn(1);
//
//        Mockito.verify(namedParameterJdbcTemplate).update(any(), any(SqlParameterSource.class),
//                captorKeyHolder.capture());
//
//        Book savedBook = bookDaoSpringJdbc.save(BookDto);
//        Assert.assertEquals(savedBook.getId(), Integer.valueOf(1));
//    }
//
//    @Test
//    public void updateTest(){
//        Book BookDto = new Book("", "", Genre.HISTORY);
//
//        Mockito.when(namedParameterJdbcTemplate.update(anyString(), any(SqlParameterSource.class))).
//                thenReturn(1);
//
//        Integer result = bookDaoSpringJdbc.update(BookDto);
//
//        Assert.assertEquals(Integer.valueOf(1), result);
//
//    }
}
