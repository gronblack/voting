package com.github.gronblack.voting.web.controller.menuItem;

import com.github.gronblack.voting.error.IllegalRequestDataException;
import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.model.MenuItem;
import com.github.gronblack.voting.model.Restaurant;
import com.github.gronblack.voting.repository.DishRepository;
import com.github.gronblack.voting.repository.MenuItemRepository;
import com.github.gronblack.voting.repository.RestaurantRepository;
import com.github.gronblack.voting.to.MenuItemTo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import static com.github.gronblack.voting.util.DateTimeUtil.currentDate;
import static com.github.gronblack.voting.util.ErrorUtil.notFound;

public abstract class BaseMenuItemController {

    @Autowired
    protected MenuItemRepository repository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    protected MenuItem prepareToSave(MenuItemTo to) {
        Dish dish = dishRepository.findById(to.getDishId())
                .orElseThrow(notFound(Dish.class, to.getDishId()));

        // check belong to restaurant
        Restaurant restaurant = restaurantRepository.findById(to.getRestaurantId())
                .orElseThrow(notFound(Restaurant.class, to.getRestaurantId()));
        if (!Objects.equals(dish.getRestaurant().getId(), restaurant.getId())) {
            throw new IllegalRequestDataException(String.format("Dish [id=%s] doesn't belong to Restaurant [id=%s]", dish.getId(), restaurant.getId()));
        }

        if (to.getActualDate() == null) {
            to.setActualDate(currentDate());
        }
        return new MenuItem(to.getId(), to.getActualDate(), dish);
    }
}
