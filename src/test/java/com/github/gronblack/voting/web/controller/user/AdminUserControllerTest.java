package com.github.gronblack.voting.web.controller.user;

import com.github.gronblack.voting.web.testdata.UserTD;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.gronblack.voting.model.Role;
import com.github.gronblack.voting.model.User;
import com.github.gronblack.voting.repository.UserRepository;
import com.github.gronblack.voting.web.BaseControllerTest;
import com.github.gronblack.voting.web.GlobalExceptionHandler;

import static com.github.gronblack.voting.web.testdata.UserTD.copy;
import static com.github.gronblack.voting.web.testdata.UserTD.user;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.gronblack.voting.web.testdata.CommonTD.NOT_FOUND_ID;

public class AdminUserControllerTest extends BaseControllerTest {
    private static final String REST_URL = AdminUserController.REST_URL + '/';

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + UserTD.ADMIN_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTD.USER_MATCHER.contentJson(UserTD.admin));
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by?email=" + UserTD.admin.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTD.USER_MATCHER.contentJson(UserTD.admin));
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTD.USER_MATCHER.contentJson(UserTD.admin, user, UserTD.userId3));
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + UserTD.USER_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findById(UserTD.USER_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + UserTD.USER_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(userRepository.getById(UserTD.USER_ID).isEnabled());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void enableNotFound() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + NOT_FOUND_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTD.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void update() throws Exception {
        User updated = UserTD.getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + UserTD.USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTD.jsonWithPassword(updated, updated.getPassword())))
                .andDo(print())
                .andExpect(status().isNoContent());

        UserTD.USER_MATCHER.assertMatch(userRepository.getById(UserTD.USER_ID), UserTD.getUpdated());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        User newUser = UserTD.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTD.jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isCreated());
        User created = UserTD.USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        UserTD.USER_MATCHER.assertMatch(created, newUser);
        UserTD.USER_MATCHER.assertMatch(userRepository.getById(newId), newUser);
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createInvalid() throws Exception {
        User invalid = new User(null, null, "", "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTD.jsonWithPassword(invalid, invalid.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void createDuplicate() throws Exception {
        User expected = new User(null, "New", UserTD.USER_MAIL, "newPass", Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTD.jsonWithPassword(expected, expected.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL)));
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        User invalid = copy(user);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL + UserTD.USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTD.jsonWithPassword(invalid, invalid.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        User updated = copy(user);
        updated.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL + UserTD.USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTD.jsonWithPassword(updated, updated.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTD.ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        User updated = copy(user);
        updated.setEmail(UserTD.ADMIN_MAIL);
        perform(MockMvcRequestBuilders.put(REST_URL + UserTD.USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserTD.jsonWithPassword(updated, updated.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_EMAIL)));
    }
}
