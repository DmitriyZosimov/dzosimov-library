package com.epam.brest.restapp;

import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.service.IReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReaderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderController.class);

    private final IReaderService readerService;

    public ReaderController(IReaderService readerService) {
        this.readerService = readerService;
    }

    /**
     * Find a reader by identification and return it
     * @param id reader identification
     * @return the found reader or null if the reader is not found
     */
    @GetMapping(value = "/reader/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReaderSample> getProfile(@PathVariable("id") Integer id){
        LOGGER.info("getProfile(id={})", id);
        ReaderSample readerSample = readerService.getProfile(id);
        return new ResponseEntity<ReaderSample>(readerSample, HttpStatus.OK);
    }

    /**
     * Find a reader without books by identification and return it
     * @param id reader identification
     * @return the found reader or null if the reader is not found
     */
    @GetMapping(value = "/reader/{id}/without_books", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReaderSample> getProfileWithoutBooks(@PathVariable("id") Integer id){
        LOGGER.info("getProfileWithoutBooks(id={})", id);
        ReaderSample readerSample = readerService.getProfileWithoutBooks(id);
        return new ResponseEntity<ReaderSample>(readerSample, HttpStatus.OK);
    }

    /**
     * Save a reader
     * @param readerSample sample of the reader who is to be saved.
     * @return sample of the saved reader
     */
    @PostMapping(value = "/reader", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReaderSample> createReader(@RequestBody ReaderSample readerSample){
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
    public ResponseEntity<Boolean> editProfile(@RequestBody ReaderSample readerSample){
        LOGGER.info("editProfile(readerSample={})", readerSample);
        Boolean result = readerService.editProfile(readerSample);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    /**
     * Update a reader from not active to active
     * @param id reader identification
     * @return true if is executed, or false if is not executed
     */
    @PutMapping(value = "/reader/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> restoreProfile(@PathVariable("id") Integer id){
        LOGGER.info("restoreProfile(id={})", id);
        Boolean result = readerService.restoreProfile(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    /**
     * Update a reader from active to not active
     * @param id reader identification
     * @return true if is executed, or false if is not executed
     */
    @DeleteMapping(value = "/reader/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> deleteProfile(@PathVariable("id") Integer id){
        LOGGER.info("deleteProfile(id={})", id);
        Boolean result = readerService.removeProfile(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }



}
