package com.github.gronblack.voting.web.controller.user;

import com.github.gronblack.voting.to.UserTo;
import com.github.gronblack.voting.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.gronblack.voting.model.Role;
import com.github.gronblack.voting.model.User;
import com.github.gronblack.voting.util.UserUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.EnumSet;

import static com.github.gronblack.voting.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.gronblack.voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RegularUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@CacheConfig(cacheNames = "users")
public class RegularUserController extends BaseUserController {
    public static final String REST_URL = "/api/account";

    @GetMapping
    @Cacheable
    @Operation(summary = "Get authorized user data", tags = "account")
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get auth user {}", authUser.id());
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(key = "#authUser.username")
    @Operation(summary = "Delete yourself", tags = "account")
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser.id());
        super.delete(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CachePut(key = "#userTo.email")
    @Operation(summary = "Register yourself", tags = "account")
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = prepareAndSave(UserUtil.createNewFromTo(userTo));
        created.setRoles(EnumSet.of(Role.USER));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CachePut(key = "#authUser.username")
    @Operation(summary = "Update authorized user data", tags = "account")
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody @Valid UserTo userTo) {
        log.info("update {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        User user = authUser.getUser();
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }
}
