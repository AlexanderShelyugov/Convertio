package ru.alexander.convertio.web.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Builder
@Value
@JsonInclude(NON_EMPTY)
public class ConversionResult {
    String sourceCurrency;
    String targetCurrency;
    Double sourceAmount;
    Double targetAmount;
    String message;
}
