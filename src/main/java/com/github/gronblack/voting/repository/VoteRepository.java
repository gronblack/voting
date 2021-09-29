package com.github.gronblack.voting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.gronblack.voting.model.Rating;
import com.github.gronblack.voting.model.Restaurant;
import com.github.gronblack.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId " +
            "AND (:startDate IS NULL OR v.date >= :startDate) AND (:endDate IS NULL OR v.date <= :endDate) ORDER BY v.date DESC")
    List<Vote> getByUserBetween(int userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.date = :date")
    Optional<Vote> getByUserOnDate(int userId, LocalDate date);

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE (:userId IS NULL OR v.user.id = :userId)" +
            "AND (:restaurantId IS NULL OR v.restaurant.id = :restaurantId)" +
            "AND (:startDate IS NULL OR v.date >= :startDate) AND (:endDate IS NULL OR v.date <= :endDate) ORDER BY v.date DESC")
    List<Vote> getByFilterLoadUser(Integer userId, Integer restaurantId, LocalDate startDate, LocalDate endDate);

    // https://www.baeldung.com/jpa-queries-custom-result-with-aggregation-functions#solution_constructor
    @Query("SELECT new com.github.gronblack.voting.model.Rating(v.restaurant, COUNT(v)) FROM Vote v " +
            "WHERE (:date IS NULL AND v.date = CURRENT_DATE) OR v.date = :date GROUP BY v.restaurant ORDER BY COUNT(v) DESC")
    List<Rating<Restaurant>> getRatingOnDate(LocalDate date);
}
