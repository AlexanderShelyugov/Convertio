package ru.alexander.convertio.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Money {
    String currency;
    Double amount;
}
