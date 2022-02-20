package ru.alexander.convertio.conversions.source.exchangeratesapi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexander.convertio.conversions.source.api.CurrenciesSource;

import java.util.Collection;

@Service
@RequiredArgsConstructor
class CurrenciesProvider implements CurrenciesSource {
    @Override
    public Collection<String> supportedCurrencies() {
        return null;
    }
}
