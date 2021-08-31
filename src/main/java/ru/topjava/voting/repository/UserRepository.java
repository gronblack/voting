package ru.topjava.voting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.User;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {
    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> getByEmail(String email);

//    @EntityGraph(attributePaths = {"votes"}, type = EntityGraph.EntityGraphType.LOAD) -upd
//    @Query("SELECT u FROM User u WHERE u.id=?1")
//    Optional<User> getWithVotes(int id);
}
