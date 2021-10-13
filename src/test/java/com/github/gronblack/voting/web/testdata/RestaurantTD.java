package com.github.gronblack.voting.web.testdata;

import com.github.gronblack.voting.MatcherFactory;
import com.github.gronblack.voting.model.Restaurant;

public class RestaurantTD {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menu", "votes", "dishes");
    public static final int RESTAURANT_NOMA_ID = 1;
    public static final Restaurant restaurantNoma = of(1, "Noma", "+45-3296-3297");
    public static final Restaurant restaurantMirazur = of(2, "Mirazur", "+33-492-41-8686");
    public static final Restaurant restaurantAsador = of(3, "Asador", "+7-495-953-1564");

    public static Restaurant of(Integer id, String name, String phone) {
        return new Restaurant(id, name, phone);
    }
}
