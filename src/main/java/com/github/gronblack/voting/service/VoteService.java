package com.github.gronblack.voting.service;

import com.github.gronblack.voting.repository.VoteRepository;
import org.springframework.stereotype.Service;
import com.github.gronblack.voting.model.Restaurant;
import com.github.gronblack.voting.model.Vote;
import com.github.gronblack.voting.repository.RestaurantRepository;

import static com.github.gronblack.voting.util.ErrorUtil.notFound;

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
