package com.github.gronblack.voting.web.testdata;

import com.github.gronblack.voting.MatcherFactory;
import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.model.MenuItem;
import com.github.gronblack.voting.model.Restaurant;

import java.time.LocalDate;

import static com.github.gronblack.voting.util.DateTimeUtil.currentDate;

public class MenuItemTD {
    public static final MatcherFactory.Matcher<MenuItem> MENU_ITEM_MATCHER =
            MatcherFactory.usingEqualsComparator(MenuItem.class);

    private static final LocalDate currentDate = currentDate();
    public static final int MENU_ITEM_TODAY_ID = 3;
    public static final int RESTAURANT_ASADOR_ID = 3;
    public static final int RESTAURANT_MIRAZUR_ID = 2;

    private static final Restaurant restaurantNoma = new Restaurant(1, "Noma", "+45-3296-3297");
    private static final Restaurant restaurantMirazur = new Restaurant(2, "Mirazur", "+33-4-92-41-8686");
    private static final Restaurant restaurantAsador = new Restaurant(3, "Asador", "+7-495-953-1564");

    public static final Dish dish1 = new Dish(1, "Beef Wellington", 150.25, restaurantNoma);
    public static final Dish dish3 = new Dish(3, "Peking duck", 110, restaurantNoma);
    public static final Dish dish4 = new Dish(4, "Chicken salad", 95.5, restaurantMirazur);
    public static final Dish dish5 = new Dish(5, "Caesar salad", 100, restaurantMirazur);
    public static final Dish dish6 = new Dish(6, "Potato frittata", 98.5, restaurantMirazur);
    public static final Dish dish8 = new Dish(8, "Hummus", 80, restaurantAsador);
    public static final Dish dish9 = new Dish(9, "Chocolate ice cream", 50, restaurantAsador);

    public static final MenuItem menuItemTodayID3 = new MenuItem(3, currentDate, dish1);
    public static final MenuItem menuItemTodayID4 = new MenuItem(4, currentDate, dish3);
    public static final MenuItem menuItemTodayID5 = new MenuItem(5, currentDate.minusDays(1), dish4);
    public static final MenuItem menuItemTodayID6 = new MenuItem(6, currentDate.minusDays(1), dish5);
    public static final MenuItem menuItemTodayID7 = new MenuItem(7, currentDate, dish5);
    public static final MenuItem menuItemTodayID8 = new MenuItem(8, currentDate, dish6);
    public static final MenuItem menuItemTodayID11 = new MenuItem(11, currentDate, dish8);
    public static final MenuItem menuItemTodayID12 = new MenuItem(12, currentDate, dish9);
}
