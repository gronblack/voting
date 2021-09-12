package ru.topjava.voting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.service.DishService;
import ru.topjava.voting.to.DishTo;

import javax.validation.Valid;
import java.net.URI;

import static ru.topjava.voting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.topjava.voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {
    public static final String REST_URL = "/api/admin/dishes";
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final DishService service;

    public AdminDishController(DishService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo dishTo) {
        log.info("create {}", dishTo);
        checkNew(dishTo);
        Dish created = service.saveFromTo(dishTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody DishTo dishTo, @PathVariable int id) {
        log.info("update dish from TO {}", dishTo);
        assureIdConsistent(dishTo, id);
        service.saveFromTo(dishTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }
}
