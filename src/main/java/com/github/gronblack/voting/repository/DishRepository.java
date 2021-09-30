package com.github.gronblack.voting.repository;

import com.github.gronblack.voting.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    @Query("SELECT d FROM Dish d WHERE :restaurantId IS NULL OR d.restaurant.id = :restaurantId")
    List<Dish> getByRestaurantId(Integer restaurantId);
}
