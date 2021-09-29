package com.github.gronblack.voting.web.controller.menu;

import com.github.gronblack.voting.web.testdata.MenuTD;
import com.github.gronblack.voting.web.testdata.UserTD;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.gronblack.voting.web.BaseControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.gronblack.voting.util.DateTimeUtil.currentDate;
import static com.github.gronblack.voting.web.testdata.CommonTD.NOT_FOUND_ID;

class RegularMenuControllerTest extends BaseControllerTest {
    static final String REST_URL = RegularMenuController.REST_URL + "/";

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getAllWithRestaurantBetweenWithoutParams() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTD.MENU_MATCHER.contentJson(MenuTD.menuNomaTodayID2, MenuTD.menuMirazurTodayID4, MenuTD.menuAsadorTodayID6));
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getAllWithRestaurantBetweenDates() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("startDate", String.valueOf(currentDate().minusDays(1)))
                .param("endDate", String.valueOf(currentDate().minusDays(1))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTD.MENU_MATCHER.contentJson(MenuTD.menuNomaYesterdayID1, MenuTD.menuMirazurYesterdayID3, MenuTD.menuAsadorYesterdayID5));
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getWithRestaurantDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MenuTD.MENU_NOMA_TODAY_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTD.MENU_MATCHER.contentJson(MenuTD.menuNomaTodayID2WithDishes));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}