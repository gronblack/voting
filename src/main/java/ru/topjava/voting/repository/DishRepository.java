package ru.topjava.voting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Dish;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    @EntityGraph(attributePaths = {"restaurant", "menu"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT d FROM Dish d ORDER BY d.name")
    List<Dish> getAll();

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId ORDER BY d.name")
    List<Dish> getByRestaurant(int restaurantId);

    @Query(value = "SELECT d.* FROM menu_dishes md INNER JOIN dish d ON md.dishes_id = d.id " +
            "WHERE md.menu_id = :menuId ORDER BY d.name", nativeQuery = true)
    List<Dish> getByMenu(int menuId);
}
