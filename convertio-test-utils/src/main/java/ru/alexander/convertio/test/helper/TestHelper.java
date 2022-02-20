package ru.alexander.convertio.test.helper;

import lombok.NonNull;
import lombok.val;
import org.jeasy.random.EasyRandom;
import ru.alexander.convertio.model.Money;

import java.util.Collection;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.generate;

public final class TestHelper {

    public static final EasyRandom RANDOM = new EasyRandom();

    public static Money randomMoney() {
        return Money.builder()
            .currency(randomString())
            .amount(randomAmount())
            .build();
    }

    public static Collection<String> randomCurrencies() {
        return generate(TestHelper::randomCurrency)
            .limit(12)
            .collect(toSet());
    }

    public static Collection<String> randomCurrencies(@NonNull String includingThis) {
        val currencies = randomCurrencies();
        currencies.add(includingThis);
        return currencies;
    }

    public static Map<String, String> randomCurrenciesWithDescriptions() {
        return randomCurrencies()
            .stream()
            .collect(toMap(identity(), currency -> "Description of " + currency));
    }

    public static String randomCurrency() {
        return randomString();
    }

    public static String randomString() {
        return RANDOM.nextObject(String.class);
    }

    public static Double randomAmount() {
        return RANDOM.nextDouble() * Double.MAX_VALUE;
    }

    private TestHelper() {
    }
}
