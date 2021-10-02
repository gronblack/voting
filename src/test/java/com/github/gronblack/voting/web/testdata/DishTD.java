package com.github.gronblack.voting.web.testdata;

import com.github.gronblack.voting.MatcherFactory;
import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.model.Restaurant;
import com.github.gronblack.voting.to.DishTo;

public class DishTD {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingEqualsComparator(Dish.class);
    public static final int DISH_1_ID = 1;
    public static final int DISH_9_ID = 9;
    public static final int RESTAURANT_WITHOUT_DISH_1_ID = 2;

private static final Restaurant restaurantNoma = new Restaurant(1, "Noma");
    private static final Restaurant restaurantMirazur = new Restaurant(2, "Mirazur");
    private static final Restaurant restaurantAsador = new Restaurant(3, "Asador");

    public static final Dish dish1 = new Dish(1, "Beef Wellington", 150.25, restaurantNoma);
    public static final Dish dish2 = new Dish(2, "Onion soup", 80.50, restaurantNoma);
    public static final Dish dish3 = new Dish(3, "Peking duck", 110, restaurantNoma);
    public static final Dish dish4 = new Dish(4, "Chicken salad", 95.5, restaurantMirazur);
    public static final Dish dish5 = new Dish(5, "Caesar salad", 100, restaurantMirazur);
    public static final Dish dish6 = new Dish(6, "Potato frittata", 98.5, restaurantMirazur);
    public static final Dish dish7 = new Dish(7, "Fish pie", 65.8, restaurantAsador);
    public static final Dish dish8 = new Dish(8, "Hummus", 80, restaurantAsador);
    public static final Dish dish9 = new Dish(9, "Chocolate ice cream", 50, restaurantAsador);

    public static DishTo fromDish(Dish dish) {
        return fromDish(dish, dish.getRestaurant().getId());
    }

    public static DishTo fromDish(Dish dish, Integer restaurantId) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice(), restaurantId);
    }

    public static Dish copy(Dish dish) {
        return new Dish(dish.id(), dish.getName(), dish.getPrice(), dish.getRestaurant());
    }
}
