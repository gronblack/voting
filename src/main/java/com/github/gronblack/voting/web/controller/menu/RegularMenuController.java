package com.github.gronblack.voting.web.controller.menu;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import com.github.gronblack.voting.model.Menu;

import java.time.LocalDate;
import java.util.List;

import static com.github.gronblack.voting.util.DateTimeUtil.currentDate;

@RestController
@RequestMapping(value = RegularMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RegularMenuController extends BaseMenuController {
    public static final String REST_URL = "/api/menu";

    @GetMapping
    @Operation(summary = "Get all with restaurants between dates (default - for current date)", tags = "menu")
    public List<Menu> getAllWithRestaurantBetween(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("getAllWithRestaurantBetween {} and {}", startDate, endDate);
        if (startDate == null & endDate == null) {
            startDate = currentDate();
            endDate = currentDate();
        }
        return repository.getAllLoadRestaurantBetween(startDate, endDate);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by id with restaurant and dishes", tags = "menu")
    public ResponseEntity<Menu> getWithRestaurantDishes(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.getByIdLoad(id));
    }
}
