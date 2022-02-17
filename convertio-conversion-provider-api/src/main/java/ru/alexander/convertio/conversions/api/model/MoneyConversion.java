package ru.alexander.convertio.conversions.api.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.alexander.convertio.model.Money;

@Builder
@Value
public class MoneyConversion {
    @NonNull
    Money from;
    @NonNull
    Money to;
}
