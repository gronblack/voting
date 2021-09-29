package com.github.gronblack.voting.model;

public class Rating<T>
{
    private T record;
    private long rating;

    public Rating() {
    }

    public Rating(T record, long rating) {
        this.record = record;
        this.rating = rating;
    }

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
