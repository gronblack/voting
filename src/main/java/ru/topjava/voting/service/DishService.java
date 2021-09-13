package ru.topjava.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.model.Menu;
import ru.topjava.voting.repository.DishRepository;
import ru.topjava.voting.repository.MenuRepository;
import ru.topjava.voting.repository.RestaurantRepository;
import ru.topjava.voting.to.DishTo;

import java.util.List;

@Service
public class DishService {
    private final RestaurantRepository restRepo;
    private final DishRepository dishRepo;
    private final MenuRepository menuRepo;

    public DishService(RestaurantRepository restaurantRepository, DishRepository dishRepository, MenuRepository menuRepository) {
        this.restRepo = restaurantRepository;
        this.dishRepo = dishRepository;
        this.menuRepo = menuRepository;
    }

    public Dish get(int id) {
        return dishRepo.findById(id).orElseThrow();
    }

    public Dish saveFromTo(DishTo to) {
        Dish dish = new Dish(to.getId(), to.getName(), to.getPrice(), restRepo.findById(to.getRestaurant_id()).orElseThrow());
        return dishRepo.save(dish);
    }

    public void removeAllDishesFromMenu(int restaurantId) {
        // https://www.baeldung.com/convert-array-to-list-and-list-to-array#1-using-plain-java
        removeFromMenu(restaurantId, dishRepo.getAllByRestaurantId(restaurantId).toArray(new Dish[0]));
    }

    public void removeFromMenu(int restaurantId, Dish... dishes) {
        List<Menu> menus = menuRepo.getByRestaurantId(restaurantId);
        menus.forEach(menu -> menu.removeDishes(dishes));
        menuRepo.saveAllAndFlush(menus);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(int id) {
        Dish dish = dishRepo.getById(id);
        removeFromMenu(dish.getRestaurant().id(), dish);
        dishRepo.deleteExisted(id);
    }
}
