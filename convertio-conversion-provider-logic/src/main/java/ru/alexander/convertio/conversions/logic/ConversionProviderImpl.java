package ru.alexander.convertio.conversions.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;
import ru.alexander.convertio.model.Money;

@Service
@RequiredArgsConstructor
class ConversionProviderImpl implements ConversionProvider {
    private final ApiKeyVault apiKeyVault;
    private final RestTemplate http;

    @Override
    public MoneyConversion convert(Money source, String targetCurrency) {
        return null;
    }
}
