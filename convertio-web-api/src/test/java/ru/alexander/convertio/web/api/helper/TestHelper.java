package ru.alexander.convertio.web.api.helper;

import org.jeasy.random.EasyRandom;

import static java.util.UUID.randomUUID;

public final class TestHelper {

    public static final EasyRandom RANDOM = new EasyRandom();

    public static String randomString() {
        return randomUUID().toString();
    }

    public static Double randomAmount() {
        return 42.0;
//        return RANDOM.nextDouble() * Double.MAX_VALUE;
    }

    private TestHelper() {
    }
}
