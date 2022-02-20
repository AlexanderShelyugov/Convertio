package ru.alexander.convertio.conversions.source.api;

import java.util.Collection;
import java.util.Map;

public interface CurrenciesSource {
    Collection<String> supportedCurrencies();
    Map<String, String> currenciesWithDescription();
}
