package ru.alexander.convertio.conversions.source.api;

import ru.alexander.convertio.model.Money;

public interface ConversionSource {
    Money convert(Money from, String targetCurrency);
}
