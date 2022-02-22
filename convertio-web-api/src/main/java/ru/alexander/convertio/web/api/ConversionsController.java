package ru.alexander.convertio.web.api;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.alexander.convertio.conversions.api.ConversionFailedException;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.CurrenciesService;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;
import ru.alexander.convertio.model.Money;
import ru.alexander.convertio.web.api.model.ConversionResult;

import javax.validation.ValidationException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
class ConversionsController implements ConversionsApi {
    private static final String MSG_INCORRECT_CURRENCY = "Currency \"%s\" is not supported";

    private final CurrenciesService currenciesService;
    private final ConversionProvider conversionProvider;

    public ResponseEntity<ConversionResult> convertMoney(
        String sourceCurrency,
        String targetCurrency,
        Double sourceAmount
    ) {
        checkCurrency(sourceCurrency);
        checkCurrency(targetCurrency);
        MoneyConversion conversion;
        try {
            conversion = conversionProvider.convert(
                Money.builder().currency(sourceCurrency).amount(sourceAmount).build(),
                targetCurrency
            );
        } catch (ConversionFailedException e) {
            throw new RuntimeException(e.getMessage());
        }
        val result = new ConversionResult()
            .sourceCurrency(conversion.getFrom().getCurrency())
            .sourceAmount(conversion.getFrom().getAmount().doubleValue())
            .targetCurrency(conversion.getTo().getCurrency())
            .targetAmount(conversion.getTo().getAmount().doubleValue());
        return ok().body(result);
    }

    private void checkCurrency(String currency) {
        // TODO replace with custom validator
        if (currency == null ||
            currency.trim().equalsIgnoreCase("null") ||
            currency.isBlank() ||
            !currenciesService.isCurrencySupported(currency)) {
            val msg = String.format(MSG_INCORRECT_CURRENCY, currency);
            throw new ValidationException(msg);
        }
    }
}
