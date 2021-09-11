package ru.topjava.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.repository.DishRepository;
import ru.topjava.voting.repository.MenuRepository;
import ru.topjava.voting.repository.RestaurantRepository;
import ru.topjava.voting.to.DishTo;

@Service
public class DishService {
    private final RestaurantRepository restRepo;
    private final DishRepository dishRepo;
    private final MenuRepository menuRepo;

    public DishService(RestaurantRepository restaurantRepository, DishRepository dishRepo, MenuRepository menuRepo) {
        this.restRepo = restaurantRepository;
        this.dishRepo = dishRepo;
        this.menuRepo = menuRepo;
    }

    public Dish saveFromTo(DishTo to) {
        return dishRepo.save( new Dish(to.getId(), to.getName(), to.getPrice(), restRepo.getById(to.getRestaurant_id())) );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeFromMenus(int restaurantId, Dish... dishes) {
        menuRepo.getByRestaurantId(restaurantId).forEach(menu -> menu.removeDishes(dishes));
        menuRepo.flush();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(int id) {
        Dish dish = dishRepo.getById(id);
        removeFromMenus(dish.getRestaurant().id(), dish);
        dishRepo.deleteExisted(id);
    }
}
