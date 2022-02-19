package ru.alexander.convertio.conversions.logic;

import org.springframework.stereotype.Service;
import ru.alexander.convertio.conversions.api.CurrenciesService;

@Service
class CurrenciesServiceImpl implements CurrenciesService {
    @Override
    public boolean isCurrencySupported(String currency) {
        return false;
    }
}
