package ru.alexander.convertio.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alexander.convertio.conversions.api.ConversionFailedException;
import ru.alexander.convertio.conversions.api.ConversionProvider;
import ru.alexander.convertio.conversions.api.CurrenciesService;
import ru.alexander.convertio.conversions.api.model.MoneyConversion;
import ru.alexander.convertio.model.Money;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/conversions", produces = {APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
class ConversionsController {
    private static final String MSG_INCORRECT_CURRENCY = "Currency %s is not supported";

    private final CurrenciesService currenciesService;
    private final ConversionProvider conversionProvider;

    @Operation(summary = "Convert money to required currency")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conversion successful",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ConversionResult.class))}
        ),
        @ApiResponse(responseCode = "400", description = "Incorrect currencies or negative money amount",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Conversion not found",
            content = @Content)
    })
    @GetMapping("/{from}/{to}/{amount}")
    ResponseEntity<?> convert(
        @Parameter(description = "Source currency", required = true, example = "USD")
        @PathVariable("from") @NotBlank String sourceCurrency,
        @Parameter(description = "Target currency", required = true, example = "EUR")
        @PathVariable("to") @NotBlank String targetCurrency,
        @Parameter(description = "Given amount of money", required = true, example = "100")
        @PathVariable("amount") @Min(0) Double sourceAmount
    ) {
        val validationResult = ofNullable(checkCurrency(sourceCurrency))
            .orElse(checkCurrency(targetCurrency));
        if (Objects.nonNull(validationResult)) {
            return validationResult;
        }
        MoneyConversion conversion;
        try {
            conversion = conversionProvider.convert(
                Money.builder().currency(sourceCurrency).amount(sourceAmount).build(),
                targetCurrency
            );
        } catch (ConversionFailedException e) {
            return internalServerError().body(e.getMessage());
        }
        val result = ConversionResult.builder()
            .sourceCurrency(conversion.getFrom().getCurrency())
            .sourceAmount(conversion.getFrom().getAmount().doubleValue())
            .targetCurrency(conversion.getTo().getCurrency())
            .targetAmount(conversion.getTo().getAmount().doubleValue())
            .build();
        return ok().body(result);
    }

    private ResponseEntity<ConversionResult> checkCurrency(String currency) {
        // TODO replace with custom validator.
        if (currency.equalsIgnoreCase("null")) {
            return badRequest().build();
        }
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
