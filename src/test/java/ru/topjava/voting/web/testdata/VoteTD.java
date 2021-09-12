package ru.topjava.voting.web.testdata;

import ru.topjava.voting.MatcherFactory;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.model.User;
import ru.topjava.voting.model.Vote;
import ru.topjava.voting.service.VoteService;

import java.time.*;

import static ru.topjava.voting.util.validation.ValidationUtil.VOTE_TIME_BORDER;
import static ru.topjava.voting.web.testdata.RestaurantTD.restaurantMirazur;

public class VoteTD {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final int USER_VOTE_1_ID = 1;
    public static final int USER_VOTE_TODAY_ID = 6;
    public static final int ADMIN_VOTE_ID = 2;
    public static final Vote userVote1 = new Vote(1, LocalDate.of(2020, Month.MAY, 20), UserTD.user, RestaurantTD.restaurantNoma);
    public static final Vote userVote2 = new Vote(2, LocalDate.of(2020, Month.MAY, 20), UserTD.admin, RestaurantTD.restaurantNoma);
    public static final Vote userVote3 = new Vote(3, LocalDate.of(2020, Month.MAY, 20), UserTD.user2, RestaurantTD.restaurantAsador);
    public static final Vote userVote4 = new Vote(4, LocalDate.of(2020, Month.MAY, 21), UserTD.user, RestaurantTD.restaurantNoma);
    public static final Vote userVote6Today = new Vote(USER_VOTE_TODAY_ID, LocalDate.now(), UserTD.user, restaurantMirazur);

    public static final String ratingTodayJSONString = "[{\"record\":{\"id\":2,\"name\":\"Mirazur\",\"dishes\":null},\"rating\":2}]";
    public static final String ratingOnDateJSONString = "[{\"record\":{\"id\":1,\"name\":\"Noma\",\"dishes\":null},\"rating\":2},{\"record\":{\"id\":3,\"name\":\"Asador\",\"dishes\":null},\"rating\":1}]";

    public static Clock voteBorderClock(boolean before) {
        LocalTime time = VOTE_TIME_BORDER.minusMinutes(before ? 1 : 0);
        Instant instant = LocalDateTime.of(LocalDate.now(), time).toInstant(OffsetDateTime.now().getOffset());
        return Clock.fixed(instant, ZoneId.systemDefault());
    }

    public static Vote getNewVote(User user, Restaurant restaurant) {
        return new Vote(null, LocalDate.now(VoteService.getClock()), user, restaurant);
    }
}
