package ru.alexander.convertio.web.api;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ConversionResult {
    String sourceCurrency;
    String targetCurrency;
    Double sourceAmount;
    Double targetAmount;
}
