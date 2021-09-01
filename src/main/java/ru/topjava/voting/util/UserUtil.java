package ru.topjava.voting.util;

import ru.topjava.voting.model.User;

public class UserUtil {

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;
//    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

//    public static User createNewFromTo(UserTo userTo) {
//        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), userTo.getCaloriesPerDay(), Role.USER);
//    }
//
//    public static User updateFromTo(User user, UserTo userTo) {
//        user.setName(userTo.getName());
//        user.setEmail(userTo.getEmail().toLowerCase());
//        user.setCaloriesPerDay(userTo.getCaloriesPerDay());
//        user.setPassword(userTo.getPassword());
//        return user;
//    }

    public static User prepareToSave(User user) {
//        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setPassword("new pass");
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}