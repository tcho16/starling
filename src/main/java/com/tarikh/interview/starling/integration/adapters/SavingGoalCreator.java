package com.tarikh.interview.starling.integration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.GoalDTO;
import com.tarikh.interview.starling.domain.models.GoalContainer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class SavingGoalCreator {

    private final String accessToken;
    private final String starlingGoalUrl;
    private final OkHttpClient client;

    //We create the goal and return a map of <String,String> where the K = ID of the goal and V = the goalName
    public Map<String, String> createGoal(GoalContainer goalContainer){
        //Creating the request
        Request request = new Request.Builder()
                .url(buildPutGoalUrl(starlingGoalUrl, goalContainer.getAccUId()))
                .put(buildRequestBodyForCreatingGoal(goalContainer.getNameOfGoal()))
                .header("Authorization",
                        "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();

        //Create
        try {
            String response = client.newCall(request)
                    .execute()
                    .body()
                    .string();

            JSONObject jsonObject = new JSONObject(response);
            return Map.of(jsonObject.getString("savingsGoalUid"), goalContainer.getNameOfGoal());

        }catch(Exception e)
        {
            log.error("Error in fetching the list of goals", e);
            throw new RuntimeException("Error in fetching goals");
        }
    };

    @SneakyThrows
    private RequestBody buildRequestBodyForCreatingGoal(String goalName) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(GoalDTO.builder()
                .currency("GBP")
                .name(goalName)
                .build());
        System.out.println("THIS IS THE REQUEST BODY FOR CREATING A GOAL");
        System.out.println(requestBody);
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
