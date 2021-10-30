package com.github.gronblack.voting.web.controller;

import com.github.gronblack.voting.error.NotFoundException;
import com.github.gronblack.voting.model.Rating;
import com.github.gronblack.voting.model.Vote;
import com.github.gronblack.voting.repository.VoteRepository;
import com.github.gronblack.voting.service.VoteService;
import com.github.gronblack.voting.to.VoteTo;
import com.github.gronblack.voting.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.gronblack.voting.util.DateTimeUtil.currentDate;
import static com.github.gronblack.voting.util.validation.ValidationUtil.checkTime;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {
    public static final String REST_URL = "/api/votes";

    private final VoteRepository repository;
    private final VoteService service;

    @GetMapping
    @Operation(summary = "Get votes for authorized user by filter (default - for current date)", tags = "votes")
    public List<VoteTo> getByFilter(@AuthenticationPrincipal AuthUser authUser,
                                    @RequestParam @Nullable Integer restaurantId,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        int userId = authUser.id();
        if (startDate == null & endDate == null & restaurantId == null) {
            startDate = currentDate();
            endDate = currentDate();
        }
        log.info("getByFilter: user {}, restaurant {}, dates({} - {})", userId, restaurantId, startDate, endDate);
        return repository.getByFilter(userId, restaurantId, startDate, endDate);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by id", tags = "votes")
    public ResponseEntity<VoteTo> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.get(authUser.id(), id));
    }

    @GetMapping("/rating")
    @Operation(summary = "Get restaurants rating on date (default - for current date)", tags = "votes")
    public List<Rating> getRating(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            log.info("getRating by current date");
        } else {
            log.info("getRating by date {}", date);
        }
        return repository.getRatingByDate(date);
    }

    // https://stackoverflow.com/a/55653219/16899097
    @PostMapping
    @Operation(summary = "Create vote for authorized user", tags = "votes")
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        log.info("create vote for user {}, restaurant {}", authUser.id(), restaurantId);
        Vote v = new Vote(currentDate(), authUser.getUser());
        Vote created = service.save(v, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update vote for authorized user", tags = "votes")
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        int userId = authUser.id();
        log.info("update vote for user {}, restaurant {}", userId, restaurantId);
        checkTime();
        Vote v = repository.getByUserAndDate(userId, currentDate())
                .orElseThrow(() -> new NotFoundException(String.format("Not found Vote today for User[%s]", authUser.getUser().getEmail())));
        service.save(v, restaurantId);
    }
}
