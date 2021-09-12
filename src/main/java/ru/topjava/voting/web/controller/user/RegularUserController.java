package ru.topjava.voting.web.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.voting.model.User;
import ru.topjava.voting.to.UserTo;
import ru.topjava.voting.util.UserUtil;
import ru.topjava.voting.web.AuthUser;

import javax.validation.Valid;
import java.net.URI;

import static ru.topjava.voting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.topjava.voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RegularUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
//@CacheConfig(cacheNames = "users")
public class RegularUserController extends BaseUserController {
    public static final String REST_URL = "/api/account";

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get auth user {}", authUser.id());
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser.id());
        super.delete(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    //@CacheEvict(allEntries = true)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = prepareAndSave(UserUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    //@CacheEvict(allEntries = true)
    public void update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        User user = authUser.getUser();
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }
}
