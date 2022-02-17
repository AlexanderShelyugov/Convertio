package ru.alexander.convertio.conversions.api;

import ru.alexander.convertio.conversions.api.model.MoneyConversion;
import ru.alexander.convertio.model.Money;

public interface ConversionProvider {
    MoneyConversion convert(Money source, String targetCurrency);
}
