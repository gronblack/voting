package ru.topjava.voting.web;

import ru.topjava.voting.MatcherFactory;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.model.Role;
import ru.topjava.voting.model.User;
import ru.topjava.voting.model.Vote;
import ru.topjava.voting.service.VoteService;
import ru.topjava.voting.util.JsonUtil;

import java.time.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ru.topjava.voting.util.validation.ValidationUtil.VOTE_TIME_BORDER;

public class TestData {
    public static final int EXIST_ID = 1;
    public static final int NOT_FOUND_ID = 100500;

    public static class ForUser {
        public static final MatcherFactory.Matcher<User> USER_MATCHER =
                MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "votes", "password");
        public static final int USER_ID = 1;
        public static final int ADMIN_ID = 2;
        public static final String USER_MAIL = "user@gmail.com";
        public static final String ADMIN_MAIL = "admin@voting.ru";
        public static final User user = new User(USER_ID, "User", USER_MAIL, "password", Role.USER);
        public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);

        public static User getNewUser() {
            return new User(null, "New", "new@gmail.com", "{noop}newPassword", false, new Date(), Collections.singleton(Role.USER));
        }

        public static User getUpdated() {
            return new User(USER_ID, "UpdatedName", USER_MAIL, "{noop}newPassword", false, new Date(), List.of(Role.ADMIN));
        }

        public static String jsonWithPassword(User user, String passw) {
            return JsonUtil.writeAdditionProps(user, "password", passw);
        }
    }

    public static class ForRestaurant {
        public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
                MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus", "votes", "dishes");
        public static final int RESTAURANT_NOMA_ID = 1;
        public static final Restaurant restaurantNoma = new Restaurant(1, "Noma");
        public static final Restaurant restaurantMirazur = new Restaurant(2, "Mirazur");
        public static final Restaurant restaurantAsador = new Restaurant(3, "Asador");
    }

    public static class ForVote {
        public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
        public static final int USER_VOTE_1_ID = 1;
        public static final int USER_VOTE_TODAY_ID = 5;
        public static final int ADMIN_VOTE_ID = 2;
        public static final Vote userVote1 = new Vote(1, LocalDate.of(2020, Month.MAY, 20), ForUser.user, ForRestaurant.restaurantNoma);
        public static final Vote userVote3 = new Vote(3, LocalDate.of(2020, Month.MAY, 21), ForUser.user, ForRestaurant.restaurantNoma);
        public static final Vote userVote5Today = new Vote(USER_VOTE_TODAY_ID, LocalDate.now(), ForUser.user, ForRestaurant.restaurantMirazur);

        public static Clock voteBorderClock(boolean before) {
            LocalTime time = VOTE_TIME_BORDER.minusMinutes(before ? 1 : 0);
            Instant instant = LocalDateTime.of(LocalDate.now(), time).toInstant(OffsetDateTime.now().getOffset());
            return Clock.fixed(instant, ZoneId.systemDefault());
        }

        public static Vote getNewVote(User user, Restaurant restaurant) {
            return new Vote(null, LocalDate.now(VoteService.getClock()), user, restaurant);
        }
    }
}
