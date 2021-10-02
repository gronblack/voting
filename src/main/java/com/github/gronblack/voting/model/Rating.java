package com.github.gronblack.voting.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rating<T>
{
    private T data;
    private int rating;

    public Rating(T data, long rating) {
        this.data = data;
        this.rating = (int) rating;
    }
}