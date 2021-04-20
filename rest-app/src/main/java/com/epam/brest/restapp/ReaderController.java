package com.epam.brest.restapp;

import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.sample.SearchReaderSample;
import com.epam.brest.service.ReaderService;
import com.epam.brest.service.SearchReaderValidator;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class ReaderController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReaderController.class);

  private final ReaderService readerService;
  private final SearchReaderValidator searchReaderValidator;

  public ReaderController(ReaderService readerService,
      SearchReaderValidator searchReaderValidator) {
    this.readerService = readerService;
    this.searchReaderValidator = searchReaderValidator;
  }

  @GetMapping(value = "/reader", produces = "application/json")
  public ResponseEntity<List<ReaderSample>> getAllReaders() {
    LOGGER.info("getAllReaders()");
    List<ReaderSample> readers = readerService.findAll();
    return new ResponseEntity<>(readers, HttpStatus.OK);
  }

  /**
   * Find a reader by identification and return it
   *
   * @param id reader identification
   * @return the found reader or {@link HttpStatus} NOT FOUND
   */
  @GetMapping(value = "/reader/{id}", produces = "application/json")
  public ResponseEntity<ReaderSample> findReaderById(@PathVariable("id") @Min(1) Integer id) {
    LOGGER.info("findReaderById(id={})", id);
    ReaderSample readerSample = readerService.findReaderById(id);
    return readerSample != null ? new ResponseEntity<>(readerSample, HttpStatus.OK)
        : new ResponseEntity("The reader was not found", HttpStatus.NOT_FOUND);
  }

  /**
   * Find a reader without books by identification and return it
   *
   * @param id reader identification
   * @return the found reader or {@link HttpStatus} NOT FOUND
   */
  @GetMapping(value = "/reader/{id}/without_books", produces = "application/json")
  public ResponseEntity<ReaderSample> findReaderByIdWithoutBooks(
      @PathVariable("id") @Min(1) Integer id) {
    LOGGER.info("findReaderByIdWithoutBooks(id={})", id);
    ReaderSample readerSample = readerService.findReaderByIdWithoutBooks(id);
    return readerSample != null ? new ResponseEntity<>(readerSample, HttpStatus.OK)
        : new ResponseEntity("The reader was not found", HttpStatus.NOT_FOUND);
  }

  /**
   * Save a reader
   *
   * @param readerSample sample of the reader who is to be saved.
   * @return sample of the saved reader
   */
  @PostMapping(value = "/reader", consumes = "application/json", produces = "application/json")
  public ResponseEntity<ReaderSample> createReader(@Valid @RequestBody ReaderSample readerSample) {
    LOGGER.info("createReader(readerSample={})", readerSample);
    ReaderSample savedReaderSample = readerService.createReader(readerSample);
    return new ResponseEntity<>(savedReaderSample, HttpStatus.OK);
  }

  /**
   * Update a reader
   *
   * @param readerSample sample of the reader who is to be updated
   * @return true if is executed, or false if is not executed
   */
  @PutMapping(value = "/reader", consumes = "application/json", produces = "application/json")
  public ResponseEntity<Boolean> editReader(@Valid @RequestBody ReaderSample readerSample) {
    LOGGER.info("editReader(readerSample={})", readerSample);
    Boolean result = readerService.editReader(readerSample);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * Update a reader from not active to active
   *
   * @param id reader identification
   * @return true if is executed, , or {@link HttpStatus} BAD REQUEST
   */
  @PutMapping(value = "/reader/{id}", produces = "application/json")
  public ResponseEntity<Boolean> changeReaderToActive(@PathVariable("id") @Min(1) Integer id) {
    LOGGER.info("changeReaderToActive(id={})", id);
    Boolean result = readerService.changeReaderToActive(id);
    return result ? new ResponseEntity<>(true, HttpStatus.OK)
        : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
  }

  /**
   * Update a reader from active to not active
   *
   * @param id reader identification
   * @return true if is executed, or {@link HttpStatus} BAD REQUEST
   */
  @DeleteMapping(value = "/reader/{id}", produces = "application/json")
  public ResponseEntity<Boolean> changeReaderToNoActive(@PathVariable("id") @Min(1) Integer id) {
    LOGGER.info("changeReaderToNoActive(id={})", id);
    Boolean result = readerService.changeReaderToNoActive(id);
    return result ? new ResponseEntity<>(true, HttpStatus.OK)
        : new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
  }

  @PostMapping(value = "/readers/search", consumes = "application/json",
      produces = "application/json")
  public ResponseEntity<List<ReaderSample>> searchReadersByDate(
      @RequestBody SearchReaderSample searchReaderSample, BindingResult bindingResult) {
    LOGGER.info("searchReadersByDate(SearchReaderSample={})", searchReaderSample);
    searchReaderValidator.validate(searchReaderSample, bindingResult);
    return (bindingResult.hasErrors()) ?
        new ResponseEntity(Objects.requireNonNull(bindingResult.getFieldError("from"))
            .getDefaultMessage(), HttpStatus.BAD_REQUEST) :
        new ResponseEntity<>(readerService.searchReaders(searchReaderSample), HttpStatus.OK);
  }
}
