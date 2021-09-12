package ru.topjava.voting.web.controller.menu;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.model.Menu;

import javax.validation.Valid;
import java.net.URI;

import static ru.topjava.voting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.topjava.voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController extends BaseMenuController {
    public static final String REST_URL = "/api/admin/menu";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody Menu menu) {
        log.info("create {}", menu);
        checkNew(menu);
        Menu created = repository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int id) {
        log.info("update {}", menu);
        assureIdConsistent(menu, id);
        repository.save(menu);
    }

    @PatchMapping("/{id}/add-dish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void addDish(@RequestBody Dish dish, @PathVariable int id) {
        log.info("addDish to menu {}", id);
        repository.get(id).orElseThrow().addDishes(dish);
    }

    @PatchMapping("/{id}/remove-dish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void removeDish(@RequestBody Dish dish, @PathVariable int id) {
        log.info("removeDish from menu {}", id);
        repository.get(id).orElseThrow().removeDishes(dish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }
}
