package com.tarikh.interview.starling.integration.controllers;

import com.tarikh.interview.starling.api.GoalTimeframeDTO;
import com.tarikh.interview.starling.api.ResponseDTO;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;
import com.tarikh.interview.starling.domain.ports.PublishRoundUpPort;
import com.tarikh.interview.starling.integration.converters.TimestampDTOToTimestampDurationConverter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RoundUpController {
    private final PublishRoundUpPort roundUpHandler;
    private final TimestampDTOToTimestampDurationConverter converter;

    @Autowired
    public RoundUpController(PublishRoundUpPort publishRoundUpPort, TimestampDTOToTimestampDurationConverter converter) {
        this.roundUpHandler = publishRoundUpPort;
        this.converter = converter;
    }

    @PutMapping("account/{accHolderUId}/saving-goals/transactions/roundup")
    public ResponseEntity<ResponseDTO> putRoundedUpTransactions(@NonNull @PathVariable String accHolderUId,
                                                                @NonNull @RequestBody GoalTimeframeDTO goalTimeframeDTO) {
        log.info("postTransactions:+ received request={} for accHolderId={}", goalTimeframeDTO, accHolderUId);
        GoalTimeframe goalTimeframe = converter.convert(accHolderUId, goalTimeframeDTO);

        roundUpHandler.publishToGoal(goalTimeframe);

        log.info("postTransactions:-");
        return new ResponseEntity<>(buildResponse("Successfully deposited money into goal"), HttpStatus.ACCEPTED);
    }

    private ResponseDTO buildResponse(String messageOfPublishingToGoal) {
        return ResponseDTO.builder()
                .message(messageOfPublishingToGoal)
                .build();
    }
}
