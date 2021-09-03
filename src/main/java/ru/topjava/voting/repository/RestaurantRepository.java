package ru.topjava.voting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Restaurant;

import java.util.List;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    //@EntityGraph(attributePaths = {"menus"}, type = EntityGraph.EntityGraphType.FETCH)
    //List<Restaurant> findAll(Sort sort);
}
