package com.github.gronblack.voting.web.controller.menuItem;

import com.github.gronblack.voting.model.MenuItem;
import com.github.gronblack.voting.to.MenuItemTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.github.gronblack.voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminMenuItemController extends BaseMenuItemController {
    public static final String REST_URL = "/api/admin/menu-items";

    @GetMapping("/{id}")
    @Operation(summary = "Get by id", tags = "menu-items")
    public ResponseEntity<MenuItem> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create", tags = "menu-items")
    public ResponseEntity<MenuItem> createWithLocation(@Valid @RequestBody MenuItemTo to) {
        log.info("create from to {}", to);
        checkNew(to);
        MenuItem created = repository.save(prepareToSave(to));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete", tags = "menu-items")
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }
}
