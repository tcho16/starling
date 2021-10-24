package com.tarikh.interview.starling.integration.controllers;

import com.tarikh.interview.starling.api.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

   @PostMapping("account/{accHolderUId}/saving-goals/transactions/roundup")
   public ResponseDTO postTransactions(@NonNull @PathVariable String accHolderUId,
                                       @NonNull @RequestBody GoalTimeframeDTO goalTimeframeDTO)
   {
      log.info("postTransactions:+ received request={} for accHolderId={}", goalTimeframeDTO, accHolderUId);
      GoalTimeframe goalTimeframe = converter.convert(accHolderUId, goalTimeframeDTO);

      String messageOfPublishingToGoal = roundUpHandler.publishToGoal(goalTimeframe);
      log.info("postTransactions:-");
      ResponseDTO responseDTO = buildResponse(messageOfPublishingToGoal);
      return responseDTO;
   }

   private ResponseDTO buildResponse(String messageOfPublishingToGoal) {
      return ResponseDTO.builder()
              .message(messageOfPublishingToGoal)
              .build();
   }
}
