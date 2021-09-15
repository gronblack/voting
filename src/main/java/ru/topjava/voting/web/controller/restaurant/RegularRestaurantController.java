package ru.topjava.voting.web.controller.restaurant;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.topjava.voting.model.Restaurant;

import java.util.List;

@RestController
@RequestMapping(value = RegularRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegularRestaurantController extends BaseRestaurantController {
    public static final String REST_URL = "/api/restaurants";

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @GetMapping("/with-dishes")
    public List<Restaurant> getAllWithDishes() {
        log.info("getAllWithDishes");
        return repository.getAllLoadDishes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping("/{id}/with-dishes")
    public ResponseEntity<Restaurant> getWithDishes(@PathVariable int id) {
        log.info("getWithDishes {}", id);
        return ResponseEntity.of(repository.getByIdLoadDishes(id));
    }
}
