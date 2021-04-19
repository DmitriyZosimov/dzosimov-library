package com.epam.brest.restapp;

import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.sample.SearchReaderSample;
import com.epam.brest.service.ReaderService;
import com.epam.brest.service.SearchReaderValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
public class ReaderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderController.class);

    private final ReaderService readerService;
    private final SearchReaderValidator searchReaderValidator;

    public ReaderController(ReaderService readerService, SearchReaderValidator searchReaderValidator) {
        this.readerService = readerService;
        this.searchReaderValidator = searchReaderValidator;
    }

    @GetMapping(value = "/reader", produces = "application/json")
    public ResponseEntity<List<ReaderSample>> getAllReaders(){
        LOGGER.info("getAllReaders()");
        List<ReaderSample> readers = readerService.findAll();
        return new ResponseEntity<List<ReaderSample>>(readers, HttpStatus.OK);
    }

    /**
     * Find a reader by identification and return it
     * @param id reader identification
     * @return the found reader or {@link HttpStatus} NOT FOUND
     */
    @GetMapping(value = "/reader/{id}", produces = "application/json")
    public ResponseEntity<ReaderSample> getProfile(@PathVariable("id") @Min(1) Integer id){
        LOGGER.info("getProfile(id={})", id);
        ReaderSample readerSample = readerService.getProfile(id);
        return readerSample != null ? new ResponseEntity<ReaderSample>(readerSample, HttpStatus.OK)
                : new ResponseEntity("The reader was not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Find a reader without books by identification and return it
     * @param id reader identification
     * @return the found reader or {@link HttpStatus} NOT FOUND
     */
    @GetMapping(value = "/reader/{id}/without_books", produces = "application/json")
    public ResponseEntity<ReaderSample> getProfileWithoutBooks(@PathVariable("id") @Min(1) Integer id){
        LOGGER.info("getProfileWithoutBooks(id={})", id);
        ReaderSample readerSample = readerService.getProfileWithoutBooks(id);
        return readerSample != null ? new ResponseEntity<ReaderSample>(readerSample, HttpStatus.OK)
                : new ResponseEntity("The reader was not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Save a reader
     * @param readerSample sample of the reader who is to be saved.
     * @return sample of the saved reader
     */
    @PostMapping(value = "/reader", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReaderSample> createReader(@Valid @RequestBody ReaderSample readerSample){
        LOGGER.info("createReader(readerSample={})", readerSample);
        ReaderSample savedReaderSample = readerService.createReader(readerSample);
        return new ResponseEntity<ReaderSample>(savedReaderSample, HttpStatus.OK);
    }

    /**
     * Update a reader
     * @param readerSample sample of the reader who is to be updated
     * @return true if is executed, or false if is not executed
     */
    @PutMapping(value = "/reader", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> editProfile(@Valid @RequestBody ReaderSample readerSample){
        LOGGER.info("editProfile(readerSample={})", readerSample);
        Boolean result = readerService.editProfile(readerSample);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    /**
     * Update a reader from not active to active
     * @param id reader identification
     * @return true if is executed, , or {@link HttpStatus} BAD REQUEST
     */
    @PutMapping(value = "/reader/{id}", produces = "application/json")
    public ResponseEntity<Boolean> restoreProfile(@PathVariable("id") @Min(1) Integer id){
        LOGGER.info("restoreProfile(id={})", id);
        Boolean result = readerService.restoreProfile(id);
        return result ? new ResponseEntity<Boolean>(result, HttpStatus.OK)
                : new ResponseEntity<Boolean>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * Update a reader from active to not active
     * @param id reader identification
     * @return true if is executed, or {@link HttpStatus} BAD REQUEST
     */
    @DeleteMapping(value = "/reader/{id}", produces = "application/json")
    public ResponseEntity<Boolean> deleteProfile(@PathVariable("id") @Min(1) Integer id){
        LOGGER.info("deleteProfile(id={})", id);
        Boolean result = readerService.removeProfile(id);
        return result ? new ResponseEntity<Boolean>(result, HttpStatus.OK)
                : new ResponseEntity<Boolean>(result, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/readers/search", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<ReaderSample>> searchReadersByDate(@RequestBody SearchReaderSample searchReaderSample,
                                                                  BindingResult bindingResult){
        LOGGER.info("searchReadersByDate(SearchReaderSample={})", searchReaderSample);
        searchReaderValidator.validate(searchReaderSample, bindingResult);
        if(bindingResult.hasErrors()){
            return new ResponseEntity(bindingResult.getFieldError("from").getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(readerService.searchReaders(searchReaderSample), HttpStatus.OK);
    }
}
