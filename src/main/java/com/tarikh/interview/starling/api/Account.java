package com.tarikh.interview.starling.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    @JsonProperty("accountUid")
    public String accountUId;

    @JsonProperty("accountType")
    public String accountType;

    @JsonProperty("defaultCategory")
    public String categoryId;

}
