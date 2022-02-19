package ru.alexander.convertio.conversions.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;
import ru.alexander.convertio.model.Money;

@Service
@RequiredArgsConstructor
class ConversionProviderImpl implements ConversionProvider {
    private final ApiKeyVault apiKeyVault;

    @Override
    public MoneyConversion convert(Money source, String targetCurrency) {
        return null;
    }
}
