package ru.topjava.voting.web.controller.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.voting.repository.DishRepository;

public abstract class BaseDishController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected DishRepository repository;
}
