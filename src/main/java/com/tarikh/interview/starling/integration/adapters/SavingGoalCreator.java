package com.tarikh.interview.starling.integration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.GoalDTO;
import com.tarikh.interview.starling.domain.ports.SavingGoalCreatorPort;
import com.tarikh.interview.starling.integration.exceptions.UnableToCreateGoalException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class SavingGoalCreator implements SavingGoalCreatorPort {

    private final String accessToken;
    private final String starlingGoalUrl;
    private final OkHttpClient client;

    @Override
    public Map<String, String> createGoal(String accUId, String nameOfGoal) {// GoalContainer goalContainer){
        //Creating the request to send
        Request request = getRequest(accUId, nameOfGoal);

        //Call the starling endpoint to create the goal
        try {
            String response = client.newCall(request)
                    .execute()
                    .body()
                    .string();

            JSONObject jsonObject = new JSONObject(response);
            return Map.of(jsonObject.getString("savingsGoalUid"), nameOfGoal);

        } catch (Exception e) {
            log.error("Error in creating new goal for user", e);
            throw new UnableToCreateGoalException("Error in creating goal for account" + accUId);
        }
    }

    @NotNull
    private Request getRequest(String accUId, String nameOfGoal) {
        return new Request.Builder()
                .url(buildPutGoalUrl(starlingGoalUrl, accUId))
                .put(buildRequestBodyForCreatingGoal(nameOfGoal))
                .header("Authorization",
                        "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();
    }

    @SneakyThrows
    private RequestBody buildRequestBodyForCreatingGoal(String goalName) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(GoalDTO.builder()
                .currency("GBP")
                .name(goalName)
                .build());

        return RequestBody.create(requestBody, MediaType.parse("application/json"));
    }

    @SneakyThrows
    private URL buildPutGoalUrl(String starlingGoalUrl, String accountUId) {
        URL finalURL = UriComponentsBuilder.fromHttpUrl(starlingGoalUrl)
                .buildAndExpand(accountUId)
                .toUri()
                .toURL();
        return finalURL;
    }
}
