package ru.alexander.convertio.conversions.source.api;

import java.util.Collection;

public interface CurrenciesSource {
    Collection<String> supportedCurrencies();
}
