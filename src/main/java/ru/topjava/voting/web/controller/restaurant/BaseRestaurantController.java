package ru.topjava.voting.web.controller.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.voting.repository.RestaurantRepository;

public abstract class BaseRestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestaurantRepository repository;

//    protected ResponseEntity<Restaurant> getWithVotes(int id) {
//        log.info("getWithVotes {}", id);
//        return ResponseEntity.of(repository.getWithVotes(id));
//    }
//
//    protected ResponseEntity<RestaurantTo> getWithRating(int id) {
//        log.info("getWithRating {}", id);
//        return ResponseEntity.of(repository.getWithRating(id));
//    }
//
//    protected List<RestaurantTo> getAllWithRating() {
//        log.info("getAllWithRating");
//        return repository.getAllWithRating();
//    }
}
