package ru.topjava.voting.web.controller.menu;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
        Menu created = repository.save(fromTo(to));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody MenuTo to) {
        log.info("update from to {}", to);
        assureIdConsistent(to, id);
        repository.save(fromTo(to));
    }

    @PatchMapping("/{id}/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void addDish(@PathVariable int id, @Valid @RequestBody @Nullable DishTo to, @RequestParam @Nullable Integer dish) {
        Menu menu = getByIdLoad(id);
        if (dish == null) {
            log.info("add new dish to menu {}", id);
            checkNew(to);
            Dish forAdd = dishService.fromTo(to);
            checkBelongToRestaurant(menu, forAdd);
            menu.addDishes(dishService.save(forAdd));
        } else {
            log.info("add existing dish {} to menu {}", dish, id);
            Dish forAdd = dishService.getById(dish);
            checkBelongToRestaurant(menu, forAdd);
            menu.addDishes(forAdd);
        }
    }

    @PatchMapping("/{id}/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void removeDish(@PathVariable int id, @RequestParam int dish) {
        log.info("removeDish from menu {}", id);
        Menu menu = getByIdLoad(id);
        Dish forRemove = dishService.getById(dish);
        checkBelongToMenu(menu, forRemove);
        menu.removeDishes(forRemove);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }
}
