package com.github.gronblack.voting.web.testdata;

import com.github.gronblack.voting.MatcherFactory;
import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.model.Menu;
import com.github.gronblack.voting.model.Restaurant;
import com.github.gronblack.voting.to.MenuTo;

import java.time.LocalDate;
import java.util.List;

import static com.github.gronblack.voting.util.DateTimeUtil.currentDate;

public class MenuTD {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingEqualsComparator(Menu.class);

    private static final LocalDate currentDate = currentDate();
    public static final int MENU_NOMA_TODAY_ID = 2;
    public static final int MENU_NOMA_1_ID = 1;
    public static final int DISH_NOMA_FREE_ID = 2;
    public static final int DISH_ASADOR_ID = 9;

    public static final Restaurant restaurantNoma = new Restaurant(1, "Noma");
    public static final Restaurant restaurantMirazur = new Restaurant(2, "Mirazur");
    public static final Restaurant restaurantAsador = new Restaurant(3, "Asador");

    public static final Dish dishNoma1 = new Dish(1, "Beef Wellington", 150.25, restaurantNoma);
    public static final Dish dishNoma3 = new Dish(3, "Peking duck", 110, restaurantNoma);
    public static final Dish dishNomaFree = new Dish(2, "Onion soup", 80.50, restaurantNoma);

    public static final Menu menuNomaTodayID2 = new Menu(2, "Noma lunch 2", currentDate, restaurantNoma);
    public static final Menu menuNomaTodayID2WithDishes = new Menu(2, "Noma lunch 2", currentDate, restaurantNoma, List.of(dishNoma1, dishNoma3));
    public static final Menu menuMirazurTodayID4 = new Menu(4, "Mirazur lunch 2", currentDate, restaurantMirazur);
    public static final Menu menuAsadorTodayID6 = new Menu(6, "Asador lunch 2", currentDate, restaurantAsador);

    public static final Menu menuNomaYesterdayID1 = new Menu(1, "Noma lunch 1", currentDate.minusDays(1), restaurantNoma);
    public static final Menu menuMirazurYesterdayID3 = new Menu(3, "Mirazur lunch 1", currentDate.minusDays(1), restaurantMirazur);
    public static final Menu menuAsadorYesterdayID5 = new Menu(5, "Asador lunch 1", currentDate.minusDays(1), restaurantAsador);

    public static MenuTo fromMenu(Menu menu) {
        return new MenuTo(menu.id(), menu.getName(), menu.getRegistered(), menu.getRestaurant().getId());
    }
}
