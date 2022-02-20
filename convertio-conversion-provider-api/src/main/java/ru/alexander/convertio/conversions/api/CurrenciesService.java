package ru.alexander.convertio.conversions.api;

import java.util.Map;

public interface CurrenciesService {
    boolean isCurrencySupported(String currency);
    Map<String, String> getAllSupportedCurrencies();
}
