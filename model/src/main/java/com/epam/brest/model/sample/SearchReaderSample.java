package com.epam.brest.model.sample;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SearchReaderSample {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    public SearchReaderSample() {
    }

    public SearchReaderSample(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "SearchReaderSample{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
