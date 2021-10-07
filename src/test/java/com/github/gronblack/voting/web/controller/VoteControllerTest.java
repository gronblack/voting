package com.github.gronblack.voting.web.controller;

import com.github.gronblack.voting.model.Vote;
import com.github.gronblack.voting.repository.VoteRepository;
import com.github.gronblack.voting.util.DateTimeUtil;
import com.github.gronblack.voting.web.BaseControllerTest;
import com.github.gronblack.voting.web.GlobalExceptionHandler;
import com.github.gronblack.voting.web.testdata.VoteTD;
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

import static com.github.gronblack.voting.web.testdata.CommonTD.NOT_FOUND_ID;
import static com.github.gronblack.voting.web.testdata.RestaurantTD.RESTAURANT_NOMA_ID;
import static com.github.gronblack.voting.web.testdata.RestaurantTD.restaurantNoma;
import static com.github.gronblack.voting.web.testdata.UserTD.*;
import static com.github.gronblack.voting.web.testdata.VoteTD.copy;
import static com.github.gronblack.voting.web.testdata.VoteTD.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends BaseControllerTest {
    private static final String REST_URL = VoteController.REST_URL + '/';

    @Autowired
    private VoteRepository repository;

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByFilter() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT_NOMA_ID))
                .param("startDate", "2020-05-20")
                .param("endDate", "2020-05-21"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(userVoteTo4, userVoteTo1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getRating() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "rating"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RATING_MATCHER.contentJson(ratingToday));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getRatingOnDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "rating")
                .param("date", "2020-05-20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RATING_MATCHER.contentJson(ratingOnDate));
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
        void update() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(true));
            Vote updated = copy(userVote6Today);
            updated.setRestaurant(restaurantNoma);
            perform(MockMvcRequestBuilders.put(REST_URL)
                    .param("restaurantId", String.valueOf(RESTAURANT_NOMA_ID)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            VOTE_MATCHER.assertMatch(repository.getById(USER_VOTE_TODAY_ID), updated);
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void updateNotFound() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(true));
            perform(MockMvcRequestBuilders.put(REST_URL)
                    .param("restaurantId", String.valueOf(NOT_FOUND_ID)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @WithUserDetails(value = USER_MAIL)
        void updateAfterTimeBorder() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(false));
            perform(MockMvcRequestBuilders.put(REST_URL)
                    .param("restaurantId", String.valueOf(RESTAURANT_NOMA_ID)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_TOO_LATE_FOR_VOTING)));
        }

        @Test
        @WithUserDetails(value = USER_ID3_MAIL)
        void createWithLocation() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(true));
            ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                    .param("restaurantId", String.valueOf(RESTAURANT_NOMA_ID)))
                    .andExpect(status().isCreated());
            Vote created = VOTE_MATCHER.readFromJson(action);
            int newId = created.id();
            Vote nv = VoteTD.getNewVote(userId3, restaurantNoma);
            nv.setId(newId);
            VOTE_MATCHER.assertMatch(created, nv);
            VOTE_MATCHER.assertMatch(repository.getById(newId), nv);
        }

        @Test
        @WithUserDetails(value = USER_ID3_MAIL)
        void createAfterTimeBorderIfWasNoVoteToday() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(false));
            ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                    .param("restaurantId", String.valueOf(RESTAURANT_NOMA_ID)))
                    .andDo(print())
                    .andExpect(status().isCreated());
            Vote created = VOTE_MATCHER.readFromJson(action);
            int newId = created.id();
            Vote nv = VoteTD.getNewVote(userId3, restaurantNoma);
            nv.setId(newId);
            VOTE_MATCHER.assertMatch(created, nv);
            VOTE_MATCHER.assertMatch(repository.getById(newId), nv);
        }

        @Test
        @WithUserDetails(value = ADMIN_MAIL)
        void createWithNotFound() throws Exception {
            DateTimeUtil.setClock(voteBorderClock(true));
            perform(MockMvcRequestBuilders.post(REST_URL)
                    .param("restaurantId", String.valueOf(NOT_FOUND_ID)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @Transactional(propagation = Propagation.NEVER)
        @WithUserDetails(value = USER_MAIL)
        void createDuplicate() {
            DateTimeUtil.setClock(voteBorderClock(true));
            assertThrows(Exception.class, () ->
                    perform(MockMvcRequestBuilders.post(REST_URL)
                            .param("restaurantId", String.valueOf(RESTAURANT_NOMA_ID)))
                            .andDo(print())
                            .andExpect(status().isUnprocessableEntity()));
        }
    }
}