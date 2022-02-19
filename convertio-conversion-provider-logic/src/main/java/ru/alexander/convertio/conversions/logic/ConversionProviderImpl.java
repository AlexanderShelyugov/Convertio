package ru.alexander.convertio.conversions.logic;

import org.springframework.stereotype.Service;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;
import ru.alexander.convertio.model.Money;

@Service
class ConversionProviderImpl implements ConversionProvider {
    @Override
    public MoneyConversion convert(Money source, String targetCurrency) {
        return null;
    }
}
