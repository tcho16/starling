package com.tarikh.interview.starling.integration.controllers;

import com.tarikh.interview.starling.api.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tarikh.interview.starling.api.GoalTimeframeDTO;
import com.tarikh.interview.starling.domain.PublishRoundUpPort;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;
import com.tarikh.interview.starling.integration.converters.TimestampDTOToTimestampDurationConverter;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RoundUpController
{
   private final PublishRoundUpPort roundUpHandler;
   private final TimestampDTOToTimestampDurationConverter converter;

   @Autowired
   public RoundUpController(PublishRoundUpPort publishRoundUpPort, TimestampDTOToTimestampDurationConverter converter)
   {
      this.roundUpHandler = publishRoundUpPort;
      this.converter = converter;
   }

   @PutMapping("account/{accHolderUId}/saving-goals/transactions/roundup")
   public ResponseEntity<ResponseDTO> putRoundedUpTransactions(@NonNull @PathVariable String accHolderUId,
                                                  @NonNull @RequestBody GoalTimeframeDTO goalTimeframeDTO)
   {
      log.info("postTransactions:+ received request={} for accHolderId={}", goalTimeframeDTO, accHolderUId);
      GoalTimeframe goalTimeframe = converter.convert(accHolderUId, goalTimeframeDTO);

      boolean successfullySentMoneyToGoal = roundUpHandler.publishToGoal(goalTimeframe);

      log.info("postTransactions:-");
      if(successfullySentMoneyToGoal)
      {
         return new ResponseEntity<>(buildResponse("Successfully deposited money into goal"),
                                                   HttpStatus.ACCEPTED);
      } else {
         return new ResponseEntity<>(buildResponse("Could not deposit money"),
                 HttpStatus.EXPECTATION_FAILED);
      }
   }

   private ResponseDTO buildResponse(String messageOfPublishingToGoal) {
      return ResponseDTO.builder()
              .message(messageOfPublishingToGoal)
              .build();
   }
}
