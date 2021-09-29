package com.github.gronblack.voting.web.controller.restaurant;

import com.github.gronblack.voting.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRestaurantController {

    @Autowired
    protected RestaurantRepository repository;
}
