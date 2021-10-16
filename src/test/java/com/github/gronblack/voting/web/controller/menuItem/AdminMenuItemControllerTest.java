package com.github.gronblack.voting.web.controller.menuItem;

import com.github.gronblack.voting.model.MenuItem;
import com.github.gronblack.voting.repository.MenuItemRepository;
import com.github.gronblack.voting.to.MenuItemTo;
import com.github.gronblack.voting.util.JsonUtil;
import com.github.gronblack.voting.web.BaseControllerTest;
import com.github.gronblack.voting.web.testdata.UserTD;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.gronblack.voting.web.testdata.CommonTD.NOT_FOUND_ID;
import static com.github.gronblack.voting.web.testdata.MenuItemTD.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuItemControllerTest extends BaseControllerTest {
    static final String REST_URL = AdminMenuItemController.REST_URL + "/";

    @Autowired
    private MenuItemRepository repository;

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU_ITEM_TODAY_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(menuItemTodayID3));
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItemTo newTo = new MenuItemTo(menuItemTodayID5.getId(), menuItemTodayID5.getActualDate(), menuItemTodayID5.getDish().getRestaurant().getId(), menuItemTodayID5.getDish().getId());
        newTo.setId(null);
        newTo.setActualDate(null);
        MenuItem newMenuItem = new MenuItem(menuItemTodayID5.getId(), menuItemTodayID5.getActualDate(), menuItemTodayID5.getDish());
        newMenuItem.setId(null);
        newMenuItem.setActualDate(null);

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isCreated());
        MenuItem created = MENU_ITEM_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItem.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, newMenuItem);
        MENU_ITEM_MATCHER.assertMatch(repository.getById(newId), newMenuItem);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuItemTo invalid = new MenuItemTo(null, null, 0, 0);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU_ITEM_TODAY_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(MENU_ITEM_TODAY_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}