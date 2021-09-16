package ru.topjava.voting.web.controller.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.voting.model.Dish;
import ru.topjava.voting.model.Menu;
import ru.topjava.voting.repository.MenuRepository;
import ru.topjava.voting.to.DishTo;
import ru.topjava.voting.to.MenuTo;
import ru.topjava.voting.util.JsonUtil;
import ru.topjava.voting.web.BaseControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.voting.web.testdata.CommonTD.NOT_FOUND_ID;
import static ru.topjava.voting.web.testdata.DishTD.fromDish;
import static ru.topjava.voting.web.testdata.MenuTD.*;
import static ru.topjava.voting.web.testdata.RestaurantTD.restaurantNoma;
import static ru.topjava.voting.web.testdata.UserTD.ADMIN_MAIL;
import static ru.topjava.voting.web.testdata.UserTD.USER_MAIL;

class AdminMenuControllerTest extends BaseControllerTest {
    static final String REST_URL = AdminMenuController.REST_URL + "/";

    @Autowired
    private MenuRepository repository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuTo newTo = fromMenu(menuNomaTodayID2);
        newTo.setId(null);
        newTo.setName("New menu");
        Menu newMenu = new Menu(newTo.getName(), newTo.getRegistered(), restaurantNoma);

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isCreated());
        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(repository.getById(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuTo invalid = new MenuTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuTo to = fromMenu(menuNomaTodayID2);
        to.setName("Updated");
        Menu menu = new Menu(menuNomaTodayID2);
        menu.setName("Updated");

        perform(MockMvcRequestBuilders.put(REST_URL + MENU_NOMA_TODAY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(to)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(repository.getById(MENU_NOMA_TODAY_ID), menu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        MenuTo invalid = fromMenu(menuNomaTodayID2);
        invalid.setName("");

        perform(MockMvcRequestBuilders.put(REST_URL + MENU_NOMA_TODAY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        MenuTo updated = fromMenu(menuNomaTodayID2);
        updated.setName("<script>alert(123)</script>");

        perform(MockMvcRequestBuilders.put(REST_URL + MENU_NOMA_TODAY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addExistingDish() throws Exception {
        Menu menu = new Menu(menuNomaTodayID2WithDishes);
        menu.addDishes(new Dish(dishNomaFree));

        perform(MockMvcRequestBuilders.patch(REST_URL + MENU_NOMA_TODAY_ID + "/add")
                .param("dish", String.valueOf(DISH_NOMA_FREE_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(repository.getById(MENU_NOMA_TODAY_ID), menu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addDishFromAnotherRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + MENU_NOMA_TODAY_ID + "/add")
                .param("dish", String.valueOf(DISH_ASADOR_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void addNewDish() throws Exception {
        Menu menu = new Menu(menuNomaTodayID2WithDishes);
        Dish newDish = new Dish(null, "New Dish", 52.5, restaurantNoma);
        menu.addDishes(newDish);
        DishTo newTo = fromDish(newDish);

        perform(MockMvcRequestBuilders.patch(REST_URL + MENU_NOMA_TODAY_ID + "/add")
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(repository.getById(MENU_NOMA_TODAY_ID), menu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void removeDish() throws Exception {
        Menu menu = new Menu(menuNomaTodayID2WithDishes);
        menu.removeDishes(dishNoma1);

        perform(MockMvcRequestBuilders.patch(REST_URL + MENU_NOMA_TODAY_ID + "/remove")
                .param("dish", String.valueOf(MENU_NOMA_1_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_MATCHER.assertMatch(repository.getById(MENU_NOMA_TODAY_ID), menu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void removeDishNotBelong() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + MENU_NOMA_TODAY_ID + "/remove")
                .param("dish", String.valueOf(DISH_ASADOR_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU_NOMA_TODAY_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(MENU_NOMA_TODAY_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}