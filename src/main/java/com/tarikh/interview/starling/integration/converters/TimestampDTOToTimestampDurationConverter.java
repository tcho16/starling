package com.tarikh.interview.starling.integration.converters;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.tarikh.interview.starling.integration.exceptions.BadException;
import org.springframework.stereotype.Component;

import com.tarikh.interview.starling.api.GoalTimeframeDTO;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;

//This converter converts the given timestamp to a timestamp duration domain specific object which consists of a week in advance.

@Slf4j
@Component
public class TimestampDTOToTimestampDurationConverter
{

   public GoalTimeframe convert(String accountHolderId, GoalTimeframeDTO dto)
   {
      log.info("convert:+ converting to goalTimeframe from dto={}", dto);

      validate(dto);

      GoalTimeframe goalTimeframe = GoalTimeframe.builder()
                                                  .accountHolderId(accountHolderId)
                                                  .goalName(dto.getSavingGoalName())
                                                  .timestampBegin(formatTimestamp(dto.getTimestamp()))
                                                  .build();
      log.info("convert:- built goaltimeframe={}", goalTimeframe);
      return goalTimeframe;
   }

   //This is the validation we perform. We throw a custom exception which will be dealt with the
   //GlobalExcceptionHandler class
   private void validate(GoalTimeframeDTO dto) {
      if(null == dto.getSavingGoalName() || dto.getSavingGoalName().isEmpty())
      {
         throw new BadException("Goal name must be present");
      }

      if (null == dto.getTimestamp() || dto.getTimestamp().isEmpty())
      {
         throw new BadException("Timestamp must be present");
      }
   }

   private Instant formatTimestamp(String timestamp)
   {
      final DateTimeFormatter formatter = DateTimeFormatter
                                             .ofPattern("yyyy-MM-dd HH:mm:ss")
                                             .withZone(ZoneId.systemDefault());
      return Instant.from(formatter.parse(timestamp));

   }

}
