package com.tarikh.interview.starling.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {

    @JsonProperty("accountUid")
    private int transactionAmount;

}
