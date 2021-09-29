package com.github.gronblack.voting.web;

import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;
import com.github.gronblack.voting.model.User;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public class AuthUser extends org.springframework.security.core.userdetails.User {

    @ToString.Include
    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public int id() {
        return user.id();
    }
}
