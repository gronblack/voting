package com.github.gronblack.voting.web.controller.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.gronblack.voting.model.Restaurant;

import java.util.List;

@RestController
@RequestMapping(value = RegularRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RegularRestaurantController extends BaseRestaurantController {
    public static final String REST_URL = "/api/restaurants";

    @GetMapping
    @Operation(summary = "Get all", tags = "restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by id", tags = "restaurants")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }
}
