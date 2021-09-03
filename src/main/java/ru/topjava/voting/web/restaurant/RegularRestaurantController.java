package ru.topjava.voting.web.restaurant;

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
    static final String REST_URL = "/api/restaurants";

    @GetMapping
    //@Cacheable -upd
    public List<Restaurant> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        return super.get(id);
    }
}
