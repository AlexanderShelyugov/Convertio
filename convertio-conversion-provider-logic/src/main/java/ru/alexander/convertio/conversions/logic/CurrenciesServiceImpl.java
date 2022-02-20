package ru.alexander.convertio.conversions.logic;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.alexander.convertio.conversions.api.CurrenciesService;
import ru.alexander.convertio.conversions.source.api.CurrenciesSource;

@Service
@RequiredArgsConstructor
class CurrenciesServiceImpl implements CurrenciesService {
    private final CurrenciesSource currenciesSource;

    @Override
    public boolean isCurrencySupported(String currency) {
        val supportedCurrencies = currenciesSource.supportedCurrencies();
        return supportedCurrencies.contains(currency);
    }


}
