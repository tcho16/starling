package com.tarikh.interview.starling.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Accounts {
    @JsonProperty("accounts")
    List<Account> accountList;
}
