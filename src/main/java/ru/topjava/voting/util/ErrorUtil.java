package ru.topjava.voting.util;

import ru.topjava.voting.error.NotFoundException;

import java.util.function.Supplier;

public class ErrorUtil {
    public static Supplier<NotFoundException> notFound(Class clazz, int id) {
        return () -> new NotFoundException("Not found " + clazz.getSimpleName() + " with id=" + id);
    }
}
