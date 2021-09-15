package ru.topjava.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.model.Menu;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.repository.DishRepository;
import ru.topjava.voting.repository.MenuRepository;
import ru.topjava.voting.repository.RestaurantRepository;
import ru.topjava.voting.to.DishTo;

import java.util.List;

import static ru.topjava.voting.util.ErrorUtil.notFound;

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

    public Dish getById(int id) {
        return repository.findById(id).orElseThrow(notFound(Dish.class, id));
    }

    public Dish save(Dish dish) {
        return repository.save(dish);
    }

    public Dish fromTo(DishTo to) {
        Restaurant restaurant = restRepository.findById(to.getRestaurant_id())
                .orElseThrow(notFound(Restaurant.class, to.getRestaurant_id()));
        return new Dish(to.getId(), to.getName(), to.getPrice(), restaurant);
    }

    public void removeAllDishesFromMenu(int restaurantId) {
        // https://www.baeldung.com/convert-array-to-list-and-list-to-array#1-using-plain-java
        removeFromMenu(restaurantId, repository.getAllByRestaurantId(restaurantId).toArray(new Dish[0]));
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
