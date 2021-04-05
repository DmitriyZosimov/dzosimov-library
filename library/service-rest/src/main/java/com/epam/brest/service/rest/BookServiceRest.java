package com.epam.brest.service.rest;

import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class BookServiceRest implements IBookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceRest.class);

    private String url;
    private RestTemplate restTemplate;

    public BookServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    /*
    get /book
     */
    @Override
    public List<BookSample> findAll() {
        LOGGER.info("findAll()");
        ResponseEntity responseEntity = restTemplate
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<BookSample>>(){});
        return (List<BookSample>) responseEntity.getBody();
    }
    /*
    post /book/{bookId}/reader/{readerId}
     */
    @Override
    public Boolean addReaderForBook(Integer readerId, Integer bookId) {
        LOGGER.info("addReaderForBook(readerId={},bookId={})", readerId, bookId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<BookSample> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url + "/" + bookId + "/reader/" + readerId,
                HttpMethod.POST, entity, Boolean.class);
        return responseEntity.getBody();
    }

    /*
    delete /book/{id}
     */
    @Override
    public Boolean delete(Integer bookId) {
        LOGGER.info("delete(bookId={})", bookId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<BookSample> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Boolean> responseEntity = restTemplate.
                exchange(url + "/" + bookId, HttpMethod.DELETE, entity, Boolean.class);
        return responseEntity.getBody();
    }

    /*
    post /book
     */
    @Override
    public Boolean createBook(BookSample bookSample) {
        LOGGER.info("createBook(BookSample={})", bookSample);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<BookSample> entity = new HttpEntity<>(bookSample, httpHeaders);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Boolean.class);
        return responseEntity.getBody();
    }

    /*
    get /book/search
     */
    @Override
    public List<BookSample> searchBooks(SearchBookSample bookSample) {
        LOGGER.info("searchBooks(SearchBookSample={})", bookSample);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<SearchBookSample> entity = new HttpEntity<>(bookSample, httpHeaders);
        ResponseEntity responseEntity = restTemplate.exchange(url + "/search",
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<BookSample>>(){});
        return (List<BookSample>) responseEntity.getBody();
    }
    /*
    delete /book/{bookId}/reader/{readerId}
    */
    @Override
    public Boolean removeFieldReaderFromBook(Integer bookId, Integer readerId) {
        LOGGER.info("removeFieldReaderFromBook(readerId={},bookId={})", readerId, bookId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<BookSample> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url + "/" + bookId + "/reader/" + readerId,
                HttpMethod.DELETE, entity, Boolean.class);
        return responseEntity.getBody();
    }

    /*
    put /book
     */
    @Override
    public Boolean editBook(BookSample bookSample) {
        LOGGER.info("editBook(BookSample={})", bookSample);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<BookSample> entity = new HttpEntity<>(bookSample, httpHeaders);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, Boolean.class);
        return responseEntity.getBody();
    }
    /*
    get /book/{id}
     */
    @Override
    public BookSample findBookById(Integer id) {
        ResponseEntity<BookSample> response = restTemplate.getForEntity(url + "/" + id, BookSample.class);
        return  response.getBody();
    }
}
