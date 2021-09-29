package com.github.gronblack.voting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Rating<T>
{
    private T record;
    private long rating;
}