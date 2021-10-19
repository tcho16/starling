package com.tarikh.interview.starling.api;

import java.time.Instant;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Data
@JsonDeserialize
public class TimestampDTO
{
   private String timestamp;
}
