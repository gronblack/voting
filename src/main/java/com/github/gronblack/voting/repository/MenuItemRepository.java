package com.github.gronblack.voting.repository;

import com.github.gronblack.voting.model.MenuItem;
import com.github.gronblack.voting.to.MenuItemTo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {
    @Query("SELECT new com.github.gronblack.voting.to.MenuItemTo(m.id, m.actualDate, m.dish.restaurant.id, m.dish.id) FROM MenuItem m " +
            "WHERE (:startDate IS NULL OR m.actualDate >= :startDate) AND (:endDate IS NULL OR m.actualDate <= :endDate)" +
            "AND (:restaurantId IS NULL OR m.dish.restaurant.id = :restaurantId) ORDER BY m.actualDate DESC, m.dish.restaurant.id")
    List<MenuItemTo> getByFilter(LocalDate startDate, LocalDate endDate, Integer restaurantId);
}
