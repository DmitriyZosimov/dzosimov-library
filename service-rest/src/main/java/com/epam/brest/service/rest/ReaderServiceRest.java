package com.epam.brest.service.rest;

import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.sample.SearchReaderSample;
import com.epam.brest.service.ReaderService;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReaderServiceRest implements ReaderService {

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
  public ReaderSample findReaderById(Integer id) {
    LOGGER.info("getProfile(id={})", id);
    ResponseEntity<ReaderSample> responseEntity = restTemplate.
        getForEntity(url + "/" + id, ReaderSample.class);
    return responseEntity.getBody();
  }

  /*
  get /reader/{id}/without_books
   */
  @Override
  public ReaderSample findReaderByIdWithoutBooks(Integer id) {
    LOGGER.info("findReaderByIdWithoutBooks(id={})", id);
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
  public Boolean editReader(ReaderSample readerSample) {
    LOGGER.info("editReader(readerSample={})", readerSample);
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
  public Boolean changeReaderToNoActive(Integer id) {
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
  public Boolean changeReaderToActive(Integer id) {
    LOGGER.info("changeReaderToActive(id={})", id);
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity<ReaderSample> entity = new HttpEntity<>(headers);
    ResponseEntity<Boolean> response = restTemplate.exchange(url + "/" + id,
        HttpMethod.PUT, entity, Boolean.class);
    return response.getBody();
  }

  /*
  get reader
   */
  @Override
  public List<ReaderSample> findAll() {
    LOGGER.info("findAllReader()");
    ResponseEntity responseEntity = restTemplate
        .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ReaderSample>>() {
            });
    return (List<ReaderSample>) responseEntity.getBody();
  }

  /*
  post /readers/search
   */
  @Override
  public List<ReaderSample> searchReaders(SearchReaderSample searchReaderSample) {
    LOGGER.info("searchReaders(searchReaderSample={})", searchReaderSample);
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity<SearchReaderSample> entity = new HttpEntity<>(searchReaderSample, headers);
    ResponseEntity<List<ReaderSample>> response = restTemplate.exchange(url + "s/search",
        HttpMethod.POST, entity, new ParameterizedTypeReference<List<ReaderSample>>() {
        });
    return response.getBody();
  }


}
