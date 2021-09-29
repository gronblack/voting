package com.github.gronblack.voting.web.controller.menu;

import com.github.gronblack.voting.error.IllegalRequestDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.model.Menu;
import com.github.gronblack.voting.model.Restaurant;
import com.github.gronblack.voting.repository.MenuRepository;
import com.github.gronblack.voting.repository.RestaurantRepository;
import com.github.gronblack.voting.service.DishService;
import com.github.gronblack.voting.to.MenuTo;

import static com.github.gronblack.voting.util.DateTimeUtil.currentDate;
import static com.github.gronblack.voting.util.ErrorUtil.notFound;

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

    protected static void checkBelongToRestaurant(Menu menu, Dish dish) {
        if (!menu.getRestaurant().equals(dish.getRestaurant())) {
            throw new IllegalRequestDataException(String.format("Dish [id=%s] doesn't belong to Restaurant [id=%s]", dish.getId(), dish.getRestaurant().getId()));
        }
    }

    protected static void checkBelongToMenu(Menu menu, Dish dish) {
        if (!menu.getDishes().contains(dish)) {
            throw new IllegalRequestDataException(String.format("Dish [id=%s] doesn't belong to Menu [id=%s]", dish.getId(), menu.getId()));
        }
    }
}
