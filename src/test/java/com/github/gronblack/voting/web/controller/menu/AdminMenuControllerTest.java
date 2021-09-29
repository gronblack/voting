package com.github.gronblack.voting.web.controller.menu;

import com.github.gronblack.voting.model.Menu;
import com.github.gronblack.voting.web.testdata.DishTD;
import com.github.gronblack.voting.web.testdata.MenuTD;
import com.github.gronblack.voting.web.testdata.RestaurantTD;
import com.github.gronblack.voting.web.testdata.UserTD;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.repository.MenuRepository;
import com.github.gronblack.voting.to.DishTo;
import com.github.gronblack.voting.to.MenuTo;
import com.github.gronblack.voting.util.JsonUtil;
import com.github.gronblack.voting.web.BaseControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.gronblack.voting.web.testdata.CommonTD.NOT_FOUND_ID;

class AdminMenuControllerTest extends BaseControllerTest {
    static final String REST_URL = AdminMenuController.REST_URL + "/";

    @Autowired
    private MenuRepository repository;

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuTo newTo = MenuTD.fromMenu(MenuTD.menuNomaTodayID2);
        newTo.setId(null);
        newTo.setName("New menu");
        Menu newMenu = new Menu(null, newTo.getName(), newTo.getRegistered(), RestaurantTD.restaurantNoma);

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isCreated());
        Menu created = MenuTD.MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MenuTD.MENU_MATCHER.assertMatch(created, newMenu);
        MenuTD.MENU_MATCHER.assertMatch(repository.getById(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuTo invalid = new MenuTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void update() throws Exception {
        MenuTo to = MenuTD.fromMenu(MenuTD.menuNomaTodayID2);
        to.setName("Updated");
        Menu menu = MenuTD.copy(MenuTD.menuNomaTodayID2);
        menu.setName("Updated");

        perform(MockMvcRequestBuilders.put(REST_URL + MenuTD.MENU_NOMA_TODAY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(to)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MenuTD.MENU_MATCHER.assertMatch(repository.getById(MenuTD.MENU_NOMA_TODAY_ID), menu);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        MenuTo invalid = MenuTD.fromMenu(MenuTD.menuNomaTodayID2);
        invalid.setName("");

        perform(MockMvcRequestBuilders.put(REST_URL + MenuTD.MENU_NOMA_TODAY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        MenuTo updated = MenuTD.fromMenu(MenuTD.menuNomaTodayID2);
        updated.setName("<script>alert(123)</script>");

        perform(MockMvcRequestBuilders.put(REST_URL + MenuTD.MENU_NOMA_TODAY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void addExistingDish() throws Exception {
        Menu menu = MenuTD.copy(MenuTD.menuNomaTodayID2WithDishes);
        menu.addDishes(DishTD.copy(MenuTD.dishNomaFree));

        perform(MockMvcRequestBuilders.patch(REST_URL + MenuTD.MENU_NOMA_TODAY_ID + "/add")
                .param("dish", String.valueOf(MenuTD.DISH_NOMA_FREE_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MenuTD.MENU_MATCHER.assertMatch(repository.getById(MenuTD.MENU_NOMA_TODAY_ID), menu);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void addDishFromAnotherRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + MenuTD.MENU_NOMA_TODAY_ID + "/add")
                .param("dish", String.valueOf(MenuTD.DISH_ASADOR_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void addNewDish() throws Exception {
        Menu menu = MenuTD.copy(MenuTD.menuNomaTodayID2WithDishes);
        Dish newDish = new Dish(null, "New Dish", 52.5, RestaurantTD.restaurantNoma);
        menu.addDishes(newDish);
        DishTo newTo = DishTD.fromDish(newDish);

        perform(MockMvcRequestBuilders.patch(REST_URL + MenuTD.MENU_NOMA_TODAY_ID + "/add")
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MenuTD.MENU_MATCHER.assertMatch(repository.getById(MenuTD.MENU_NOMA_TODAY_ID), menu);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void removeDish() throws Exception {
        Menu menu = MenuTD.copy(MenuTD.menuNomaTodayID2WithDishes);
        menu.removeDishes(MenuTD.dishNoma1);

        perform(MockMvcRequestBuilders.patch(REST_URL + MenuTD.MENU_NOMA_TODAY_ID + "/remove")
                .param("dish", String.valueOf(MenuTD.MENU_NOMA_1_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MenuTD.MENU_MATCHER.assertMatch(repository.getById(MenuTD.MENU_NOMA_TODAY_ID), menu);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void removeDishNotBelong() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + MenuTD.MENU_NOMA_TODAY_ID + "/remove")
                .param("dish", String.valueOf(MenuTD.DISH_ASADOR_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MenuTD.MENU_NOMA_TODAY_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(MenuTD.MENU_NOMA_TODAY_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}