package ru.topjava.voting.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.repository.RestaurantRepository;

import java.util.List;

public abstract class BaseRestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestaurantRepository repository;

    protected List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    protected ResponseEntity<Restaurant> get(int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }
}
