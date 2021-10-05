package com.github.gronblack.voting.web.controller.user;

import com.github.gronblack.voting.to.UserTo;
import com.github.gronblack.voting.web.testdata.UserTD;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.gronblack.voting.model.User;
import com.github.gronblack.voting.repository.UserRepository;
import com.github.gronblack.voting.util.JsonUtil;
import com.github.gronblack.voting.util.UserUtil;
import com.github.gronblack.voting.web.BaseControllerTest;
import com.github.gronblack.voting.web.GlobalExceptionHandler;

import static com.github.gronblack.voting.web.testdata.UserTD.copy;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(UserController.REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTD.USER_MATCHER.contentJson(UserTD.user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(UserController.REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(UserController.REST_URL))
                .andExpect(status().isNoContent());
        UserTD.USER_MATCHER.assertMatch(userRepository.findAll(), UserTD.admin, UserTD.userId3);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword");
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(UserController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = UserTD.USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        UserTD.USER_MATCHER.assertMatch(created, newUser);
        UserTD.USER_MATCHER.assertMatch(userRepository.getById(newId), newUser);
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo newTo = new UserTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(UserController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", UserTD.USER_MAIL, "newPassword");
        perform(MockMvcRequestBuilders.put(UserController.REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        UserTD.USER_MATCHER.assertMatch(userRepository.getById(UserTD.USER_ID), UserUtil.updateFromTo(copy(UserTD.user), updatedTo));
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void updateInvalid() throws Exception {
        UserTo updatedTo = new UserTo(null, null, "password", null);
        perform(MockMvcRequestBuilders.put(UserController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", UserTD.ADMIN_MAIL, "newPassword");
        perform(MockMvcRequestBuilders.put(UserController.REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL)));
    }
}