package ru.alexander.convertio.conversions.source.exchangeratesapi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexander.convertio.conversions.source.api.ConversionSource;
import ru.alexander.convertio.model.Money;

@Service
@RequiredArgsConstructor
class ConversionProvider implements ConversionSource {
    @Override
    public Money convert(Money from, String targetCurrency) {
        return null;
    }
}
