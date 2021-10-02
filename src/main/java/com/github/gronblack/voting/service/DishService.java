package com.github.gronblack.voting.service;

import com.github.gronblack.voting.error.IllegalRequestDataException;
import com.github.gronblack.voting.model.Menu;
import com.github.gronblack.voting.util.ErrorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.model.Restaurant;
import com.github.gronblack.voting.repository.DishRepository;
import com.github.gronblack.voting.repository.MenuRepository;
import com.github.gronblack.voting.repository.RestaurantRepository;
import com.github.gronblack.voting.to.DishTo;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {
    private final DishRepository repository;
    private final RestaurantRepository restRepository;
    private final MenuRepository menuRepository;

    public DishService(RestaurantRepository restaurantRepository, DishRepository dishRepository, MenuRepository menuRepository) {
        this.restRepository = restaurantRepository;
        this.repository = dishRepository;
        this.menuRepository = menuRepository;
    }

    public List<Dish> getByRestaurantId(Integer restaurantId) {
        return repository.getByRestaurantId(restaurantId);
    }

    public Optional<Dish> findById(int id) {
        return repository.findById(id);
    }

    public Dish saveFromTo(DishTo to) {
        Dish dish = new Dish(to.getId(), to.getName(), to.getPrice(), restRepository.getById(to.getRestaurant_id()));
        return repository.save(dish);
    }

    public Dish checkBelong(int id, int restaurantId) {
        return repository.get(id, restaurantId).orElseThrow(
                () -> new IllegalRequestDataException("Dish id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }

    public void removeAllDishesFromMenu(int restaurantId) {
        // https://www.baeldung.com/convert-array-to-list-and-list-to-array#1-using-plain-java
        removeFromMenu(restaurantId, repository.getByRestaurantId(restaurantId).toArray(new Dish[0]));
    }

    public void removeFromMenu(int restaurantId, Dish... dishes) {
        List<Menu> menus = menuRepository.getByRestaurantIdLoadDishes(restaurantId);
        menus.forEach(menu -> menu.removeDishes(dishes));
        menuRepository.saveAllAndFlush(menus);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(int id) {
        Dish dish = repository.getById(id);
        removeFromMenu(dish.getRestaurant().id(), dish);
        repository.deleteExisted(id);
    }
}
