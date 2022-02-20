package ru.alexander.convertio.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alexander.convertio.conversions.api.CurrenciesService;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/currencies", produces = {APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
class CurrenciesController {
    private final CurrenciesService currenciesService;

    @Operation(summary = "Get all supported currencies and their descriptions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Retrieval successful",
            content = {
                @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Map.class))
            })
    })
    @GetMapping
    ResponseEntity<?> getSupportedCurrencies() {
        val currencies = currenciesService.getAllSupportedCurrencies();
        return ok(currencies);
    }
}
