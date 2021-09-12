package ru.topjava.voting.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.voting.model.Vote;
import ru.topjava.voting.repository.VoteRepository;
import ru.topjava.voting.util.DateTimeUtil;
import ru.topjava.voting.util.JsonUtil;
import ru.topjava.voting.web.controller.VoteController;
import ru.topjava.voting.web.testdata.VoteTD;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.voting.web.testdata.CommonTD.NOT_FOUND_ID;
import static ru.topjava.voting.web.testdata.RestaurantTD.*;
import static ru.topjava.voting.web.testdata.UserTD.*;
import static ru.topjava.voting.web.testdata.VoteTD.*;

class VoteControllerTest extends BaseControllerTest {
    private static final String REST_URL = VoteController.REST_URL + '/';

    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER_VOTE_1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER_VOTE_1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByFilter() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("startDate", "2020-05-20")
                .param("endDate", "2020-05-21")
                .param("user", String.valueOf(USER_ID))
                .param("restaurant", String.valueOf(restaurantNoma.id())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote4, userVote1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getMyBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "my")
                .param("startDate", "2020-05-20")
                .param("endDate", "2020-05-21"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote4, userVote1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getRating() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "rating"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(ratingTodayJSONString));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getRatingOnDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "rating")
                .param("date", "2020-05-20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(ratingOnDateJSONString));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + ADMIN_VOTE_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    // https://stackoverflow.com/a/58605274
    @Nested
    class TimeDependedTest {
        @AfterEach
        void reset() {
            DateTimeUtil.resetClock();
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void delete() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(true));
            perform(MockMvcRequestBuilders.delete(REST_URL + USER_VOTE_TODAY_ID))
                    .andExpect(status().isNoContent());
            assertFalse(repository.findById(USER_VOTE_TODAY_ID).isPresent());
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void deleteAfterTimeBorder() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(false));
            perform(MockMvcRequestBuilders.delete(REST_URL + USER_VOTE_TODAY_ID))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_TOO_LATE_FOR_VOTING)));
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void update() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(true));
            Vote updated = new Vote(userVote6Today);
            updated.setRestaurant(restaurantNoma);
            perform(MockMvcRequestBuilders.put(REST_URL + USER_VOTE_TODAY_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            VOTE_MATCHER.assertMatch(repository.getById(USER_VOTE_TODAY_ID), updated);
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void updateInvalid() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(true));
            Vote invalid = new Vote(null, null, null, null);
            perform(MockMvcRequestBuilders.put(REST_URL + USER_VOTE_TODAY_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(invalid)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void updateAfterTimeBorder() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(false));
            Vote updated = new Vote(userVote6Today);
            updated.setRestaurant(restaurantNoma);
            perform(MockMvcRequestBuilders.put(REST_URL + USER_VOTE_TODAY_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_TOO_LATE_FOR_VOTING)));
        }

        @Test
        @WithUserDetails(value = USER2_MAIL)
        void createWithLocation() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(true));
            Vote nv = VoteTD.getNewVote(user2, restaurantAsador);
            ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(nv)))
                    .andExpect(status().isCreated());
            Vote created = VOTE_MATCHER.readFromJson(action);
            int newId = created.id();
            nv.setId(newId);
            VOTE_MATCHER.assertMatch(created, nv);
            VOTE_MATCHER.assertMatch(repository.getById(newId), nv);
        }

        @Test
        @WithUserDetails(value = ADMIN_MAIL)
        void createAfterTimeBorder() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(false));
            perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(getNewVote(admin, restaurantMirazur))))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_TOO_LATE_FOR_VOTING)));
        }

        @Test
        @WithUserDetails(value = ADMIN_MAIL)
        void createInvalid() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(true));
            Vote invalid = new Vote(null, null, null, null);
            perform(MockMvcRequestBuilders.post(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(invalid)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @Transactional(propagation = Propagation.NEVER)
        @WithUserDetails(value = USER_MAIL)
        void createDuplicate() {
            DateTimeUtil.setClock(voteBorderClock(true));
            Vote duplicate = new Vote(null, userVote6Today.getDate(), user, restaurantNoma);
            assertThrows(Exception.class, () ->
                    perform(MockMvcRequestBuilders.post(REST_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.writeValue(duplicate)))
                            .andDo(print())
                            .andExpect(status().isUnprocessableEntity()));
        }
    }
}