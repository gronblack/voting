package com.github.gronblack.voting.repository;

import org.springframework.transaction.annotation.Transactional;
import com.github.gronblack.voting.model.Dish;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    List<Dish> getAllByRestaurantId(int restaurantId);
}
