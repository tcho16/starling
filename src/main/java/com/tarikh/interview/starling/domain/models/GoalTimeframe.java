package com.tarikh.interview.starling.domain.models;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@Builder
@Data
public class GoalTimeframe
{
   //private AccountDetails accountDetails;
   private String goalName;
   private String accountHolderId;
   private Instant timestampBegin;
   //private Instant timestampEnd;
}
