package ru.topjava.voting.web.controller.dish;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.topjava.voting.model.Dish;

import java.util.List;

@RestController
@RequestMapping(value = RegularDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegularDishController extends BaseDishController {
    public static final String REST_URL = "/api/dishes";

    @GetMapping
    public List<Dish> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping("/by-restaurant/{restaurantId}")
    public List<Dish> getByRestaurant(@PathVariable int restaurantId) {
        log.info("getByRestaurant {}", restaurantId);
        return repository.getByRestaurant(restaurantId);
    }

    @GetMapping("/by-menu/{menuId}")
    public List<Dish> getByMenu(@PathVariable int menuId) {
        log.info("getByMenu {}", menuId);
        return repository.getByMenu(menuId);
    }
}
