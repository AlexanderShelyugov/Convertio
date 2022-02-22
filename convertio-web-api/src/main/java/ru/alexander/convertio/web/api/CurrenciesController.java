package ru.alexander.convertio.web.api;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.alexander.convertio.conversions.api.CurrenciesService;

import java.util.TreeMap;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
class CurrenciesController implements CurrenciesApi {
    private final CurrenciesService currenciesService;

    public ResponseEntity<Object> getSupportedCurrencies() {
        val currencies = currenciesService.getAllSupportedCurrencies();
        return ok(new TreeMap<>(currencies));
    }
}
