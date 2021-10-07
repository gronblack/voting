package com.github.gronblack.voting.web.testdata;

import com.github.gronblack.voting.MatcherFactory;
import com.github.gronblack.voting.model.Rating;
import com.github.gronblack.voting.model.Restaurant;
import com.github.gronblack.voting.model.User;
import com.github.gronblack.voting.model.Vote;
import com.github.gronblack.voting.to.VoteTo;
import com.github.gronblack.voting.util.validation.ValidationUtil;

import java.time.*;

import static com.github.gronblack.voting.util.DateTimeUtil.currentDate;
import static com.github.gronblack.voting.web.testdata.RestaurantTD.restaurantMirazur;

public class VoteTD {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class, "id");
    public static final MatcherFactory.Matcher<Rating> RATING_MATCHER = MatcherFactory.usingEqualsComparator(Rating.class);

    public static final int USER_VOTE_TODAY_ID = 6;
    public static final Vote userVote6Today = new Vote(USER_VOTE_TODAY_ID, currentDate(), UserTD.user, restaurantMirazur);

    public static final VoteTo userVoteTo1 = new VoteTo(1, LocalDate.of(2020, Month.MAY, 20), RestaurantTD.RESTAURANT_NOMA_ID);
    public static final VoteTo userVoteTo4 = new VoteTo(4, LocalDate.of(2020, Month.MAY, 21), RestaurantTD.RESTAURANT_NOMA_ID);

    public static final Rating[] ratingToday = new Rating[] { new Rating(2, 2) };
    public static final Rating[] ratingOnDate = new Rating[] { new Rating(1, 2), new Rating(3, 1) };

    public static Clock voteBorderClock(boolean before) {
        LocalTime time = ValidationUtil.VOTE_TIME_BORDER.minusMinutes(before ? 1 : 0);
        Instant instant = LocalDateTime.of(currentDate(), time).toInstant(OffsetDateTime.now().getOffset());
        return Clock.fixed(instant, ZoneId.systemDefault());
    }

    public static Vote getNewVote(User user, Restaurant restaurant) {
        return new Vote(null, currentDate(), user, restaurant);
    }

    public static Vote copy(Vote vote) {
        return new Vote(vote.getId(), vote.getActualDate(), vote.getUser(), vote.getRestaurant());
    }
}
