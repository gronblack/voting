package ru.topjava.voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
