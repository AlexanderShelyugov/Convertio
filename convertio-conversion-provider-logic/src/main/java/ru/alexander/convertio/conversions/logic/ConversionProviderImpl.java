package ru.alexander.convertio.conversions.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexander.convertio.conversions.api.ConversionFailedException;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;
import ru.alexander.convertio.conversions.source.api.ConversionSource;
import ru.alexander.convertio.model.Money;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
class ConversionProviderImpl implements ConversionProvider {
    private final ConversionSource conversionSource;

    @Override
    public MoneyConversion convert(Money source, String targetCurrency)
        throws ConversionFailedException {
        Money target;
        try {
            target = requireNonNull(conversionSource.convert(source, targetCurrency));
        } catch (Exception e) {
            throw new ConversionFailedException(e.getMessage(), e);
        }
        return MoneyConversion.builder()
            .from(source)
            .to(target)
            .build();
    }

}
