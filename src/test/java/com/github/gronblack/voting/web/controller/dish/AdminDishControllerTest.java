package com.github.gronblack.voting.web.controller.dish;

import com.github.gronblack.voting.web.controller.AdminDishController;
import com.github.gronblack.voting.web.testdata.RestaurantTD;
import com.github.gronblack.voting.web.testdata.UserTD;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.gronblack.voting.model.Dish;
import com.github.gronblack.voting.repository.DishRepository;
import com.github.gronblack.voting.to.DishTo;
import com.github.gronblack.voting.util.JsonUtil;
import com.github.gronblack.voting.web.BaseControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.gronblack.voting.web.testdata.CommonTD.NOT_FOUND_ID;
import static com.github.gronblack.voting.web.testdata.DishTD.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminDishControllerTest extends BaseControllerTest {
    public static final String REST_URL = AdminDishController.REST_URL + "/";

    @Autowired
    private DishRepository repository;

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = new Dish(null, "New Dish", 52.5, RestaurantTD.restaurantNoma);
        DishTo newTo = fromDish(newDish);

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isCreated());
        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(repository.getById(newId), newDish);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createInvalid() throws Exception {
        DishTo invalid = new DishTo(null, null, 0, 0);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void update() throws Exception {
        DishTo to = fromDish(dish1);
        to.setName("Updated");
        Dish dish = copy(dish1);
        dish.setName("Updated");

        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(to)))
                .andDo(print())
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(repository.getById(DISH_1_ID), dish);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        DishTo invalid = fromDish(dish1);
        invalid.setName("");

        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        DishTo updated = fromDish(dish1);
        updated.setName("<script>alert(123)</script>");

        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_9_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(DISH_9_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}