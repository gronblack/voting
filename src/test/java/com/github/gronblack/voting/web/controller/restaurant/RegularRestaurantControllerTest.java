package com.github.gronblack.voting.web.controller.restaurant;

import com.github.gronblack.voting.web.testdata.CommonTD;
import com.github.gronblack.voting.web.testdata.RestaurantTD;
import com.github.gronblack.voting.web.testdata.UserTD;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.gronblack.voting.web.BaseControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegularRestaurantControllerTest extends BaseControllerTest {
    static final String REST_URL = RegularRestaurantController.REST_URL + "/";

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTD.RESTAURANT_MATCHER.contentJson(RestaurantTD.restaurantAsador, RestaurantTD.restaurantMirazur, RestaurantTD.restaurantNoma));
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getAllWithDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "with-dishes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTD.RESTAURANT_MATCHER.contentJson(RestaurantTD.restaurantAsadorWithDishes, RestaurantTD.restaurantMirazurWithDishes, RestaurantTD.restaurantNomaWithDishes));
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTD.RESTAURANT_NOMA_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTD.RESTAURANT_MATCHER.contentJson(RestaurantTD.restaurantNoma));
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getWithDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/1/with-dishes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTD.RESTAURANT_MATCHER.contentJson(RestaurantTD.restaurantNomaWithDishes));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTD.RESTAURANT_NOMA_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + CommonTD.NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}