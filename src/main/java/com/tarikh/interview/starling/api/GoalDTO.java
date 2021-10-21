package com.tarikh.interview.starling.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

@JsonSerialize
@Builder
public class GoalDTO {

    @JsonProperty("name")
    private String name;
    @JsonProperty("currency")
    private String currency;
//    @JsonProperty("target")
//    private Target target;
    @JsonProperty("base64EncodedPhoto")
    private String base64EncodedPhoto;

//    @JsonSerialize
//    @Builder
//    public class Target
//    {
//        @JsonProperty("base64EncodedPhoto")
//
//        private String currency;
//        private int minorUnits;
//    }
}
