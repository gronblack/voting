package ru.topjava.voting.web;

import org.springframework.lang.NonNull;
import ru.topjava.voting.model.User;

public class AuthUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public int id() {
        return user.id();
    }
}