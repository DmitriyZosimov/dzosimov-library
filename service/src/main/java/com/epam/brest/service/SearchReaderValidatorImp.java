package com.epam.brest.service;

import com.epam.brest.model.sample.SearchReaderSample;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service()
public class SearchReaderValidatorImp implements SearchReaderValidator {

  @Override
  public boolean supports(Class<?> aClass) {
    return SearchReaderSample.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    SearchReaderSample srs = (SearchReaderSample) o;
    if (srs.getFrom() == null) {
      srs.setFrom(LocalDate.of(1970, 1, 1));
    }
    if (srs.getTo() == null) {
      srs.setTo(LocalDate.now());
    }
    if (srs.getFrom().isAfter(srs.getTo())) {
      errors.rejectValue("from", "date.from.to", "date was not selected correctly");
    }
  }
}
