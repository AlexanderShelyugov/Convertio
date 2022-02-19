package ru.alexander.convertio.web.api;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.CurrenciesService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/conversions", produces = {APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
class ConversionsController {
    private static final String MSG_INCORRECT_CURRENCY = "Currency %s is not supported";

    private final CurrenciesService currenciesService;
    private final ConversionProvider conversionProvider;

    @GetMapping("/{from}/{to}/{amount}")
    ResponseEntity<ConversionResult> convert(
        @PathVariable("from") @NotBlank String sourceCurrency,
        @PathVariable("to") @NotBlank String targetCurrency,
        @PathVariable("amount") @Min(0) Double sourceAmount
    ) {
        val validationResult = ofNullable(checkCurrency(sourceCurrency))
            .orElse(checkCurrency(targetCurrency));
        if (Objects.nonNull(validationResult)) {
            return validationResult;
        }
        val result = ConversionResult.builder()
            .sourceCurrency(sourceCurrency)
            .sourceAmount(sourceAmount)
            .targetCurrency(targetCurrency)
            .targetAmount(42.0)
            .build();
        return ok().body(result);
    }

    private ResponseEntity<ConversionResult> checkCurrency(String currency) {
        // TODO replace with custom validator.
        if (currenciesService.isCurrencySupported(currency)) {
            return null;
        }
        val msg = String.format(MSG_INCORRECT_CURRENCY, currency);
        return badRequest()
            .body(ConversionResult.builder()
                .message(msg)
                .build());
    }
}
