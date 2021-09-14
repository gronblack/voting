package ru.topjava.voting.web.controller.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.voting.error.NotFoundException;
import ru.topjava.voting.model.Menu;
import ru.topjava.voting.repository.MenuRepository;
import ru.topjava.voting.repository.RestaurantRepository;
import ru.topjava.voting.service.DishService;

import static ru.topjava.voting.util.DateTimeUtil.currentDate;

public abstract class BaseMenuController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MenuRepository menuRepo;
    @Autowired
    protected RestaurantRepository restaurantRepo;
    @Autowired
    protected DishService dishService;

    protected Menu prepareToSave(Menu menu, int restaurantId) {
        if (menu.getRegistered() == null) {
            menu.setRegistered(currentDate());
        }
        menu.setRestaurant(restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Not found Restaurant with id=" + restaurantId)));
        return menu;
    }
}
