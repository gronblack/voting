package ru.topjava.voting.web.controller.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.voting.repository.MenuRepository;

public abstract class BaseMenuController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MenuRepository repository;
}
