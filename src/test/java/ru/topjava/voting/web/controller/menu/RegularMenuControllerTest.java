package ru.topjava.voting.web.controller.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.voting.web.BaseControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.voting.util.DateTimeUtil.currentDate;
import static ru.topjava.voting.web.testdata.CommonTD.NOT_FOUND_ID;
import static ru.topjava.voting.web.testdata.MenuTD.*;
import static ru.topjava.voting.web.testdata.UserTD.USER_MAIL;

class RegularMenuControllerTest extends BaseControllerTest {
    static final String REST_URL = RegularMenuController.REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllWithRestaurantBetweenWithoutParams() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menuNomaTodayID2, menuMirazurTodayID4, menuAsadorTodayID6));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllWithRestaurantBetweenDates() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("startDate", String.valueOf(currentDate().minusDays(1)))
                .param("endDate", String.valueOf(currentDate().minusDays(1))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menuNomaYesterdayID1, menuMirazurYesterdayID3, menuAsadorYesterdayID5));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getWithRestaurantDishes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU_NOMA_TODAY_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menuNomaTodayID2WithDishes));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}