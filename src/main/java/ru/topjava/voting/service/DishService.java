package ru.topjava.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.error.NotFoundException;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.model.Menu;
import ru.topjava.voting.model.Restaurant;
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

    public Dish getById(int id) {
        return dishRepo.findById(id).orElseThrow(() -> new NotFoundException("Not found Dish with id=" + id));
    }

    public Dish save(Dish dish) {
        return dishRepo.save(dish);
    }

    public Dish fromTo(DishTo to) {
        Restaurant restaurant = restRepo.findById(to.getRestaurant_id())
                .orElseThrow(() -> new NotFoundException("Not found Restaurant with id=" + to.getRestaurant_id()));
        return new Dish(to.getId(), to.getName(), to.getPrice(), restaurant);
    }

    public void removeAllDishesFromMenu(int restaurantId) {
        // https://www.baeldung.com/convert-array-to-list-and-list-to-array#1-using-plain-java
        removeFromMenu(restaurantId, dishRepo.getAllByRestaurantId(restaurantId).toArray(new Dish[0]));
    }

    public void removeFromMenu(int restaurantId, Dish... dishes) {
        List<Menu> menus = menuRepo.getByRestaurantIdLoadDishes(restaurantId);
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
