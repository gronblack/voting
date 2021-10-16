package com.github.gronblack.voting.service;

import com.github.gronblack.voting.error.IllegalRequestDataException;
import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.repository.DishRepository;
import com.github.gronblack.voting.repository.RestaurantRepository;
import com.github.gronblack.voting.to.DishTo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {
    private final DishRepository repository;
    private final RestaurantRepository restRepository;

    public DishService(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restRepository = restaurantRepository;
        this.repository = dishRepository;
    }

    public List<Dish> getByRestaurantId(Integer restaurantId) {
        return repository.getByRestaurantId(restaurantId);
    }

    public Optional<Dish> findById(int id) {
        return repository.findById(id);
    }

    public Dish saveFromTo(DishTo to) {
        Dish dish = new Dish(to.getId(), to.getName(), to.getPrice(), restRepository.getById(to.getRestaurantId()));
        return repository.save(dish);
    }

    public void checkBelong(int id, int restaurantId) {
        repository.get(id, restaurantId).orElseThrow(
                () -> new IllegalRequestDataException(String.format("Dish [id=%s] doesn't belong to Restaurant [id=%s]", id, restaurantId)));
    }

    //@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(int id) {
        repository.deleteExisted(id);
    }
}
