package ru.alexander.convertio.web.api;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alexander.convertio.conversions.api.CurrenciesService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/currencies", produces = {APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
class CurrenciesController {
    private final CurrenciesService currenciesService;

    @GetMapping
    ResponseEntity<?> getSupportedCurrencies() {
        val currencies = currenciesService.getAllSupportedCurrencies();
        return ok(currencies);
    }
}
