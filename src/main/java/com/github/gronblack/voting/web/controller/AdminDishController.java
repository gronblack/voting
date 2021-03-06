package com.github.gronblack.voting.web.controller;

import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.service.DishService;
import com.github.gronblack.voting.to.DishTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.gronblack.voting.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.gronblack.voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminDishController {
    public static final String REST_URL = "/api/admin/dishes";

    private final DishService service;

    @GetMapping
    @Operation(summary = "Get all by restaurant Id (default - all restaurants)", tags = "dishes")
    public List<Dish> getByRestaurantId(@RequestParam @Nullable Integer restaurantId) {
        log.info("getByRestaurantId: restaurantId {}", restaurantId);
        return service.getByRestaurantId(restaurantId);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get by id", tags = "dishes")
    public ResponseEntity<Dish> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(service.findById(id));
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create", tags = "dishes")
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo to) {
        log.info("create from TO {}", to);
        checkNew(to);
        Dish created = service.saveFromTo(to);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update", tags = "dishes")
    public void update(@Valid @RequestBody DishTo to, @PathVariable int id) {
        log.info("update from TO {}", to);
        assureIdConsistent(to, id);
        service.checkBelong(id, to.getRestaurantId());
        service.saveFromTo(to);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete", tags = "dishes")
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }
}
