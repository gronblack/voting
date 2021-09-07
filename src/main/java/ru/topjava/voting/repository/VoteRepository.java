package ru.topjava.voting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import ru.topjava.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId " +
            "AND (:startDate IS NULL OR v.date >= :startDate) AND (:endDate IS NULL OR v.date <= :endDate) ORDER BY v.date DESC")
    List<Vote> getBetween(int userId, LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE (:userId IS NULL OR v.user.id = :userId)" +
            "AND (:restaurantId IS NULL OR v.restaurant.id = :restaurantId)" +
            "AND (:startDate IS NULL OR v.date >= :startDate) AND (:endDate IS NULL OR v.date <= :endDate) ORDER BY v.date DESC")
    List<Vote> getByFilter(Integer userId, Integer restaurantId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT v FROM Vote v WHERE v.id = :id AND v.user.id = :userId")
    Optional<Vote> getByIdAndUser(int id, int userId);
}
