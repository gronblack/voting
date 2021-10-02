package com.github.gronblack.voting.web.testdata;

import com.github.gronblack.voting.MatcherFactory;
import com.github.gronblack.voting.model.Restaurant;

public class RestaurantTD {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menu", "votes", "dishes");
    public static final int RESTAURANT_NOMA_ID = 1;
    public static final Restaurant restaurantNoma = of(1, "Noma");
    public static final Restaurant restaurantMirazur = of(2, "Mirazur");
    public static final Restaurant restaurantAsador = of(3, "Asador");

    public static Restaurant of(Integer id, String name) {
        return new Restaurant(id, name);
    }
}
