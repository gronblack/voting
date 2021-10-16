package com.github.gronblack.voting.web.controller.menuItem;

import com.github.gronblack.voting.to.MenuItemTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.github.gronblack.voting.util.DateTimeUtil.currentDate;

@RestController
@RequestMapping(value = MenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MenuItemController extends BaseMenuItemController {
    public static final String REST_URL = "/api/menu-items";

    @GetMapping
    @Operation(summary = "Get all by filter (default - for current date, all restaurants)", tags = "menu")
    public List<MenuItemTo> getByFilter(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                        @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                        @RequestParam @Nullable Integer restaurantId) {
        log.info("getByFilter: restaurant {}, dates({} - {})", restaurantId, startDate, endDate);
        if (startDate == null && endDate == null) {
            startDate = currentDate();
            endDate = currentDate();
        }
        return repository.getByFilter(startDate, endDate, restaurantId);
    }
}
