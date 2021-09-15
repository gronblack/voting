package ru.topjava.voting.service;

import org.springframework.stereotype.Service;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.model.Vote;
import ru.topjava.voting.repository.RestaurantRepository;
import ru.topjava.voting.repository.VoteRepository;

import static ru.topjava.voting.util.ErrorUtil.notFound;

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
                .orElseThrow(notFound(Restaurant.class, restaurantId)));
        return voteRepository.save(vote);
    }
}
