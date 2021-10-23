package com.tarikh.interview.starling.integration.converters;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.tarikh.interview.starling.api.GoalTimeframeDTO;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;

import lombok.extern.slf4j.Slf4j;

//This converter converts the given timestamp to a timestamp duration domain specific object which consists of a week in advance.

@Slf4j
@Component
public class TimestampDTOToTimestampDurationConverter
{

   public GoalTimeframe convert(String accountHolderId, GoalTimeframeDTO dto)
   {
      log.info("convert:+ building the object using timestamp for accountHolderId={}", dto.getTimestamp(), accountHolderId);

      GoalTimeframe goalTimeframe = GoalTimeframe.builder()
                                                  .accountHolderId(accountHolderId)
                                                  .goalName(dto.getSavingGoalName())
                                                  .timestampBegin(formatTimestamp(dto.getTimestamp()))
                                                  .build();
      log.info("convert:- built object={}", goalTimeframe);
      return goalTimeframe;
   }

   private Instant formatTimestamp(String timestamp)
   {
      final DateTimeFormatter formatter = DateTimeFormatter
                                             .ofPattern("yyyy-MM-dd HH:mm:ss")
                                             .withZone(ZoneId.systemDefault());
      return Instant.from(formatter.parse(timestamp));

   }

}
