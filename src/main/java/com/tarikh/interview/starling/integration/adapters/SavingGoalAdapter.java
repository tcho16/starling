package com.tarikh.interview.starling.integration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.Amount;
import com.tarikh.interview.starling.api.AmountDTO;
import com.tarikh.interview.starling.api.GoalDTO;
import com.tarikh.interview.starling.domain.SavingGoalPort;
import com.tarikh.interview.starling.domain.models.GoalContainer;
import com.tarikh.interview.starling.integration.exceptions.UnableToAddMoneyToGoalException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class SavingGoalAdapter implements SavingGoalPort {

    private final String accessToken;
    private final String starlingGoalAddMoneyUrl;
    private final OkHttpClient client;
    private boolean unsuccessfulDepositToGoal = false;
    private boolean successfulDepositToGoal = true;

    @SneakyThrows
    @Override
    public boolean sendMoneyToGoal(GoalContainer goalContainer) {
        return sendMoney(goalContainer);
    }

    private boolean sendMoney(GoalContainer goalContainer)
    {
        try {
            Request request = new Request.Builder()
                    .url(addMoneyUrl(starlingGoalAddMoneyUrl, goalContainer, goalContainer.getGoalId()))
                    .put(buildRequestBodyForAddingMoneyToGoal(goalContainer))
                    .header("Authorization",
                            "Bearer " + accessToken)
                    .header("Accept", "application/json")
                    .build();

            int returnResponseCode = client.newCall(request)
                                            .execute()
                                            .networkResponse()
                                            .code();

            if(returnResponseCode != 200)
            {
                return unsuccessfulDepositToGoal;
            }

            return successfulDepositToGoal;
        }catch(Exception e)
        {
            log.error("Error in adding money to the goal", e);
            throw new UnableToAddMoneyToGoalException("Was not able to add money to the goal");
        }
    }

    @SneakyThrows
    private RequestBody buildRequestBodyForAddingMoneyToGoal(GoalContainer goalContainer) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(AmountDTO.builder()
                .amount(Amount.builder()
                                .currency("GBP")
                                .minorUnits(goalContainer.getAmountToAdd())
                                .build())
                .build());
        return RequestBody.create(requestBody, MediaType.parse("application/json"));
    }

    @SneakyThrows
    private URL addMoneyUrl(String starlingGoalUrl, GoalContainer goalContainer, String goalUid) {
        URL finalURL = UriComponentsBuilder.fromHttpUrl(starlingGoalUrl)
                .buildAndExpand(goalContainer.getAccUId(), goalUid, UUID.randomUUID().toString())
                .toUri()
                .toURL();
        return finalURL;
    }
}
