package ru.topjava.voting.service;

import org.springframework.stereotype.Service;
import ru.topjava.voting.error.NotFoundException;
import ru.topjava.voting.model.Vote;
import ru.topjava.voting.repository.RestaurantRepository;
import ru.topjava.voting.repository.VoteRepository;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(VoteRepository voteRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Vote save(Vote vote, int restaurantId) {
        vote.setRestaurant(restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Not found Restaurant with id=" + restaurantId)));
        return voteRepository.save(vote);
    }
}
