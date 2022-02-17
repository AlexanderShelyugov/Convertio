package ru.alexander.convertio.web.api;

import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/conversions", produces = {APPLICATION_JSON_VALUE})
class ConversionsController {
    @GetMapping("/{from}/{to}/{amount}")
    ResponseEntity<ConversionResult> convert(
        @PathVariable("from") String sourceCurrency,
        @PathVariable("to") String targetCurrency,
        @PathVariable("amount") Double sourceAmount
    ) {
        val result = ConversionResult.builder()
            .sourceCurrency(sourceCurrency)
            .sourceAmount(sourceAmount)
            .targetCurrency(targetCurrency)
            .targetAmount(42.0)
            .build();
        return ok().body(result);
    }
}
