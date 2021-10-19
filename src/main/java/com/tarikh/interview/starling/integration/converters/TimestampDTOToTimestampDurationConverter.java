package com.tarikh.interview.starling.integration.converters;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.tarikh.interview.starling.api.TimestampDTO;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.domain.models.TimestampDuration;

import lombok.extern.slf4j.Slf4j;

//This converter converts the given timestamp to a timestamp duration domain specific object which consists of a week in advance.

@Slf4j
@Component
public class TimestampDTOToTimestampDurationConverter
{

   public TimestampDuration convert(String accUId, TimestampDTO source)
   {
      log.info("convert:+ building the object using timestamp for accountId={}", source.getTimestamp(), accUId);

      TimestampDuration timestampDuration = TimestampDuration.builder()
                                                             .timestampBegin(formatTimestamp(source.getTimestamp()))
                                                             .accountDetails(AccountDetails.builder()
                                                                                           .accountUId(accUId)
                                                                                           .build())
                                                             .build();
      log.info("convert:- built object={}", timestampDuration);
      return timestampDuration;
   }

   private Instant formatTimestamp(String timestamp)
   {
      final DateTimeFormatter formatter = DateTimeFormatter
                                             .ofPattern("yyyy-MM-dd HH:mm:ss")
                                             .withZone(ZoneId.systemDefault());

      return Instant.from(formatter.parse(timestamp));

   }

}
