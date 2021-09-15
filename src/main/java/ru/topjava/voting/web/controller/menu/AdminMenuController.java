package ru.topjava.voting.web.controller.menu;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.voting.error.NotFoundException;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.model.Menu;
import ru.topjava.voting.to.DishTo;
import ru.topjava.voting.to.MenuTo;

import javax.validation.Valid;
import java.net.URI;

import static ru.topjava.voting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.topjava.voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController extends BaseMenuController {
    public static final String REST_URL = "/api/admin/menu";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody MenuTo to) {
        log.info("create from to {}", to);
        checkNew(to);
        Menu created = menuRepo.save(fromTo(to));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody MenuTo to, @PathVariable int id) {
        log.info("update from to {}", to);
        assureIdConsistent(to, id);
        menuRepo.save(fromTo(to));
    }

    @PatchMapping("/{id}/add-dish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void addNewDish(@Valid @RequestBody DishTo to, @PathVariable int id) {
        log.info("addNewDish to menu {}", id);
        checkNew(to);
        Menu menu = menuRepo.get(id).orElseThrow(() -> new NotFoundException("Not found Menu with id=" + id));
        menu.addDishes(dishService.save(dishService.fromTo(to)));
    }

    @PatchMapping("/{id}/add-dish/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void addExistingDish(@PathVariable int id, @PathVariable int dishId) {
        log.info("addExistingDish {} to menu {}", dishId, id);
        Dish existing = dishService.get(dishId);
        menuRepo.get(id).orElseThrow(() -> new NotFoundException("Not found Menu with id=" + id))
                .addDishes(existing);
    }

    @PatchMapping("/{id}/remove-dish/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void removeDish(@PathVariable int id, @PathVariable int dishId) {
        log.info("removeDish from menu {}", id);
        Menu menu = menuRepo.get(id).orElseThrow(() -> new NotFoundException("Not found Menu with id=" + id));
        menu.removeDishes(dishService.get(dishId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        menuRepo.deleteExisted(id);
    }
}
