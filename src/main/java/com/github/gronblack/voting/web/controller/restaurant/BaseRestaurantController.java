package com.github.gronblack.voting.web.controller.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.gronblack.voting.repository.RestaurantRepository;

public abstract class BaseRestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestaurantRepository repository;
}
