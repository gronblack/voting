package ru.topjava.voting.util;

import java.time.Clock;

public class DateTimeUtil {
    private static Clock clock; // https://stackoverflow.com/a/45833128

    static {
        resetClock();
    }

    public static void setClock(Clock clock) {
        DateTimeUtil.clock = clock;
    }

    public static Clock getClock() {
        return clock;
    }

    public static void resetClock() {
        setClock(Clock.systemDefaultZone());
    }
}
