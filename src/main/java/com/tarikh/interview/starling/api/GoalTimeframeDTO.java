package com.tarikh.interview.starling.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Data
@JsonDeserialize
public class GoalTimeframeDTO
{
   @NonNull
   private String savingGoalName;
   @NonNull
   private String timestamp;
}
