package com.tarikh.interview.starling.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

@JsonSerialize
@Builder
public class AmountDTO {
    @JsonProperty("amount")
    private Amount amount;
}