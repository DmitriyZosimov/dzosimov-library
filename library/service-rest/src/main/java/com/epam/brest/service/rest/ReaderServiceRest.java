package com.epam.brest.service.rest;

import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.service.IReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ReaderServiceRest implements IReaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderServiceRest.class);

    private String url;
    private RestTemplate restTemplate;

    public ReaderServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }
    /*
    get /reader/{id}
     */
    @Override
    public ReaderSample getProfile(Integer id) {
        LOGGER.info("getProfile(id={})", id);
        ResponseEntity<ReaderSample> responseEntity = restTemplate.
                getForEntity(url + "/" + id, ReaderSample.class);
        return responseEntity.getBody();
    }

    /*
    get /reader/{id}/without_books
     */
    @Override
    public ReaderSample getProfileWithoutBooks(Integer id) {
        LOGGER.info("getProfileWithoutBooks(id={})", id);
        ResponseEntity<ReaderSample> responseEntity = restTemplate.
                getForEntity(url + "/" + id + "/without_books", ReaderSample.class);
        return responseEntity.getBody();
    }
    /*
    post /reader
     */
    @Override
    public ReaderSample createReader(ReaderSample readerSample) {
        LOGGER.info("createReader(readerSample={})", readerSample);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ReaderSample> entity = new HttpEntity<>(readerSample, headers);
        ResponseEntity<ReaderSample> response = restTemplate.exchange(url,
                HttpMethod.POST, entity, ReaderSample.class);
        return response.getBody();
    }
    /*
    put /reader
     */
    @Override
    public Boolean editProfile(ReaderSample readerSample) {
        LOGGER.info("editProfile(readerSample={})", readerSample);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ReaderSample> entity = new HttpEntity<>(readerSample, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(url,
                HttpMethod.PUT, entity, Boolean.class);
        return response.getBody();
    }
    /*
    delete /reader/{id}
     */
    @Override
    public Boolean removeProfile(Integer id) {
        LOGGER.info("removeProfile(id={})", id);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ReaderSample> entity = new HttpEntity<>(headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(url + "/" + id,
                HttpMethod.DELETE, entity, Boolean.class);
        return response.getBody();
    }
    /*
     put /reader/{id}
      */
    @Override
    public Boolean restoreProfile(Integer id) {
        LOGGER.info("restoreProfile(id={})", id);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ReaderSample> entity = new HttpEntity<>(headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(url + "/" + id,
                HttpMethod.PUT, entity, Boolean.class);
        return response.getBody();
    }
}
