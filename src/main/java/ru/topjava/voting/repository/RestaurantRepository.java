package ru.topjava.voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
//    @EntityGraph(attributePaths = {"votes"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query("SELECT r FROM Restaurant r where r.id=?1")
//    Optional<Restaurant> getWithVotes(int id);
//
//    @Query("SELECT new ru.topjava.voting.to.RestaurantTo(r.id, r.name, COUNT(v.id)) " +
//            "FROM Restaurant r JOIN FETCH Vote v ON r.id = v.restaurant.id " +
//            "WHERE v.date = CURRENT_DATE GROUP BY r.id")
//    List<RestaurantTo> getAllWithRating();
//
//    @Query("SELECT new ru.topjava.voting.to.RestaurantTo(r.id, r.name, COUNT(v.id)) " +
//            "FROM Restaurant r JOIN FETCH Vote v ON r.id = v.restaurant.id " +
//            "WHERE r.id=?1 and v.date = CURRENT_DATE GROUP BY r.id")
//    Optional<RestaurantTo> getWithRating(int id);
}
