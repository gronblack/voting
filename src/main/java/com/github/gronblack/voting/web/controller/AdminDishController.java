package com.github.gronblack.voting.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.service.DishService;
import com.github.gronblack.voting.to.DishTo;

import javax.validation.Valid;
import java.net.URI;

import static com.github.gronblack.voting.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.gronblack.voting.util.validation.ValidationUtil.checkNew;

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
    @Operation(summary = "Create", tags = "dishes")
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo to) {
        log.info("create {}", to);
        checkNew(to);
        Dish created = service.save(service.fromTo(to));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update", tags = "dishes")
    public void update(@Valid @RequestBody DishTo to, @PathVariable int id) {
        log.info("update dish from TO {}", to);
        assureIdConsistent(to, id);
        service.save(service.fromTo(to));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete", tags = "dishes")
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }
}
