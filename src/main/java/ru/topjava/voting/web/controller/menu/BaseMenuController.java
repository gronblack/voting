package ru.topjava.voting.web.controller.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.voting.model.Menu;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.repository.MenuRepository;
import ru.topjava.voting.repository.RestaurantRepository;
import ru.topjava.voting.service.DishService;
import ru.topjava.voting.to.MenuTo;

import static ru.topjava.voting.util.DateTimeUtil.currentDate;
import static ru.topjava.voting.util.ErrorUtil.notFound;

public abstract class BaseMenuController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MenuRepository repository;
    @Autowired
    protected RestaurantRepository restaurantRepository;
    @Autowired
    protected DishService dishService;

    protected Menu fromTo(MenuTo to) {
        Restaurant restaurant = restaurantRepository.findById(to.getRestaurantId())
                .orElseThrow(notFound(Restaurant.class, to.getRestaurantId()));
        if (to.getRegistered() == null) {
            to.setRegistered(currentDate());
        }
        return new Menu(to.getId(), to.getName(), to.getRegistered(), restaurant);
    }

    protected Menu getByIdLoad(int id) {
        return repository.getByIdLoad(id).orElseThrow(notFound(Menu.class, id));
    }
}
