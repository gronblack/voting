package ru.topjava.voting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.voting.error.NotFoundException;
import ru.topjava.voting.model.Rating;
import ru.topjava.voting.model.Restaurant;
import ru.topjava.voting.model.Vote;
import ru.topjava.voting.repository.VoteRepository;
import ru.topjava.voting.service.VoteService;
import ru.topjava.voting.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.topjava.voting.util.DateTimeUtil.currentDate;
import static ru.topjava.voting.util.validation.ValidationUtil.checkTime;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    public static final String REST_URL = "/api/votes";

    private final VoteRepository repository;
    private final VoteService service;

    public VoteController(VoteRepository repository, VoteService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public List<Vote> getByFilter(@RequestParam @Nullable Integer user,
                                  @RequestParam @Nullable Integer restaurant,
                                  @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                  @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("getByFilter: dates({} - {}), user {}, restaurant {}", startDate, endDate, user, restaurant);
        return repository.getByFilterLoadUser(user, restaurant, startDate, endDate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping("/my")
    public List<Vote> getMyBetween(@AuthenticationPrincipal AuthUser authUser,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        int userId = authUser.id();
        if (startDate == null & endDate == null) {
            startDate = currentDate();
            endDate = currentDate();
        }
        log.info("getBetween dates({} - {}) for user {}", startDate, endDate, userId);
        return repository.getByUserBetween(userId, startDate, endDate);
    }

    @GetMapping("/rating")
    public List<Rating<Restaurant>> getRating(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            log.info("getRating by current date");
        } else {
            log.info("getRating by date {}", date);
        }
        return repository.getRatingBetween(date);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete vote of user {}", authUser.id());
        checkTime();
        Vote vote = repository.getByUserOnDate(authUser.id(), currentDate())
                .orElseThrow(() -> new NotFoundException(String.format("Not found Vote for User[%s] on date [%s]", authUser.getUser().getEmail(), currentDate())));
        repository.delete(vote);
    }

    // https://stackoverflow.com/a/55653219/16899097
    @PostMapping
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurant) {
        log.info("create vote for user {}, restaurant {}", authUser.id(), restaurant);
        checkTime();
        Vote v = new Vote(currentDate(), authUser.getUser());
        Vote created = service.save(v, restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurant) {
        int userId = authUser.id();
        log.info("update vote for user {}, restaurant {}", userId, restaurant);
        checkTime();
        Vote v = repository.getByUserOnDate(userId, currentDate())
                .orElseThrow(() -> new NotFoundException("Not found Vote today for User=" + authUser.getUser().getEmail()));
        service.save(v, restaurant);
    }
}
