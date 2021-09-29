package com.github.gronblack.voting.web.controller.restaurant;

import com.github.gronblack.voting.repository.RestaurantRepository;
import com.github.gronblack.voting.web.testdata.RestaurantTD;
import com.github.gronblack.voting.web.testdata.UserTD;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.gronblack.voting.model.Restaurant;
import com.github.gronblack.voting.util.JsonUtil;
import com.github.gronblack.voting.web.BaseControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.gronblack.voting.web.testdata.CommonTD.EXIST_ID;
import static com.github.gronblack.voting.web.testdata.CommonTD.NOT_FOUND_ID;

class AdminRestaurantControllerTest extends BaseControllerTest {
    static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantRepository repository;

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant nr = new Restaurant(null, "Test Restaurant");
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(nr)))
                .andExpect(status().isCreated());
        Restaurant created = RestaurantTD.RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        nr.setId(newId);
        RestaurantTD.RESTAURANT_MATCHER.assertMatch(created, nr);
        RestaurantTD.RESTAURANT_MATCHER.assertMatch(repository.getById(newId), nr);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = new Restaurant(EXIST_ID, "Updatable Restaurant");
        perform(MockMvcRequestBuilders.put(REST_URL + EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        RestaurantTD.RESTAURANT_MATCHER.assertMatch(repository.getById(EXIST_ID), updated);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(EXIST_ID, "");
        perform(MockMvcRequestBuilders.put(REST_URL + EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Restaurant updated = new Restaurant(EXIST_ID, "Updatable Restaurant");
        updated.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL + EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + EXIST_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(EXIST_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}