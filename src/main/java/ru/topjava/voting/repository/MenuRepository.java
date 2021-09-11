package ru.topjava.voting.repository;

import ru.topjava.voting.model.Menu;

import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> getByRestaurantId(int restaurantId);
}
