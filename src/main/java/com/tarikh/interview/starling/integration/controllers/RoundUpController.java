package com.tarikh.interview.starling.integration.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tarikh.interview.starling.api.TimestampDTO;
import com.tarikh.interview.starling.domain.RoundUpPort;
import com.tarikh.interview.starling.domain.models.TimestampDuration;
import com.tarikh.interview.starling.integration.converters.TimestampDTOToTimestampDurationConverter;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RoundUpController
{
   private final RoundUpPort roundUpHandler;
   private final TimestampDTOToTimestampDurationConverter converter;

   @Autowired
   public RoundUpController(RoundUpPort roundUpPort, TimestampDTOToTimestampDurationConverter converter)
   {
      this.roundUpHandler = roundUpPort;
      this.converter = converter;
   }


   @GetMapping("roundUp")
   public void roundUp()
   {

   }

   @PostMapping("{accUId}/saving-goals/transactions/roundup")
   public void postTransactions(@NonNull @PathVariable String accUId,
                                @NonNull @RequestBody TimestampDTO timestampDTO)
   {
      log.info("postTransactions:+ received request for timestamp={}", timestampDTO);
      TimestampDuration timestampDuration = converter.convert(accUId, timestampDTO);
      roundUpHandler.publishToGoal(timestampDuration);

      log.info("postTransactions:-");
   }



   //LOGIC
   //Create Payload containing timestamp
   //Fetch the list of transactions from: /api/v2/feed/account/{accountUid}/category/{categoryUid}/transactions-between
   //Once fetched the list of transaction, Fetch the list of goals and check if your goal is created
   //if goal doesn't exist, create one else input money into that goal

   //Should return 200 if successful
}
