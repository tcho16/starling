package com.tarikh.interview.starling.integration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.Amount;
import com.tarikh.interview.starling.api.AmountDTO;
import com.tarikh.interview.starling.domain.ports.SavingGoalPort;
import com.tarikh.interview.starling.domain.models.GoalContainer;
import com.tarikh.interview.starling.integration.exceptions.UnableToAddMoneyToGoalException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class SavingGoalAdapter implements SavingGoalPort {

    private final String accessToken;
    private final String starlingGoalAddMoneyUrl;
    private final OkHttpClient client;

    @SneakyThrows
    @Override
    public void sendMoneyToGoal(GoalContainer goalContainer) {
        sendMoney(goalContainer);
    }

    private void sendMoney(GoalContainer goalContainer) {
            Request request = buildRequest(goalContainer);

            int returnResponseCode = callStarlingGoalPersistApi(request);

            if (returnResponseCode != 200) {
                throw new UnableToAddMoneyToGoalException("Was not able to add money to the goal");
            }
    }

    private int callStarlingGoalPersistApi(Request request) {
        try {
            return client.newCall(request)
                                            .execute()
                                            .networkResponse()
                                            .code();
        } catch (IOException e) {
            log.error("Error in calling the Starling persist api", e);
            throw new UnableToAddMoneyToGoalException("Error in calling the Starling persist api");
        }
    }

    @NotNull
    private Request buildRequest(GoalContainer goalContainer) {
        return new Request.Builder()
                .url(addMoneyUrl(starlingGoalAddMoneyUrl, goalContainer, goalContainer.getGoalId()))
                .put(buildRequestBodyForAddingMoneyToGoal(goalContainer))
                .header("Authorization",
                        "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();
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
