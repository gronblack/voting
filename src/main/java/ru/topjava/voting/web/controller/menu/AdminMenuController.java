package ru.topjava.voting.web.controller.menu;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.model.Menu;
import ru.topjava.voting.to.DishTo;

import javax.validation.Valid;
import java.net.URI;

import static ru.topjava.voting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.topjava.voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController extends BaseMenuController {
    public static final String REST_URL = "/api/admin/menu";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody Menu menu, @RequestParam int restaurant) {
        log.info("create {}", menu);
        checkNew(menu);
        Menu created = menuRepo.save(prepareToSave(menu, restaurant));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int id, @RequestParam int restaurant) {
        log.info("update {}", menu);
        assureIdConsistent(menu, id);
        prepareToSave(menu, restaurant);
        menuRepo.save(menu);
    }

    @PatchMapping("/{id}/add-dish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void addNewDish(@Valid @RequestBody DishTo to, @PathVariable int id) {
        log.info("addNewDish to menu {}", id);
        checkNew(to);
        Menu menu = menuRepo.get(id).orElseThrow();
        menu.addDishes(dishService.saveFromTo(to));
    }

    @PatchMapping("/{id}/add-dish/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void addExistingDish(@PathVariable int id, @PathVariable int dishId) {
        log.info("addExistingDish {} to menu {}", dishId, id);
        Dish existing = dishService.get(dishId);
        menuRepo.get(id).orElseThrow().addDishes(existing);
    }

    @PatchMapping("/{id}/remove-dish/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void removeDish(@PathVariable int id, @PathVariable int dishId) {
        log.info("removeDish from menu {}", id);
        Menu menu = menuRepo.get(id).orElseThrow();
        menu.removeDishes(dishService.get(dishId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        menuRepo.deleteExisted(id);
    }
}
