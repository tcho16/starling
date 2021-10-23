package com.tarikh.interview.starling.integration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.Amount;
import com.tarikh.interview.starling.api.AmountDTO;
import com.tarikh.interview.starling.api.GoalDTO;
import com.tarikh.interview.starling.domain.SavingGoalPort;
import com.tarikh.interview.starling.domain.models.GoalUpdater;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
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
    private final String starlingGoalUrl;
    private final OkHttpClient client;

    @Override
    public void sendMoneyToGoal(GoalUpdater goalUpdater) {
        log.info("sendMoneyToGoal:+ Sending this to the goal={}", goalUpdater);
        //First fetch goals ? if exist then update : create goal then send amount

        HashMap<String, String> listOfSavingGoals = fetchCurrentGoalsDetails(goalUpdater.getAccUId());
        if(savingGoalDoesNotExists(goalUpdater, listOfSavingGoals))
        {
            //create the goal
            System.out.println("Creating the goal as goal is not present");
            Map<String, String> goal = createGoal(goalUpdater);
            for (Map.Entry<String, String> stringStringEntry : goal.entrySet()) {
                listOfSavingGoals.put(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }

        //Sending money to goal
        sendMoney(goalUpdater, fetchSavingGoalUId(goalUpdater, listOfSavingGoals));

    }

    private String fetchSavingGoalUId(GoalUpdater goalUpdater, HashMap<String, String> listOfSavingGoals) {
        for (Map.Entry<String, String> stringStringEntry : listOfSavingGoals.entrySet()) {
            if(stringStringEntry.getValue().equalsIgnoreCase(goalUpdater.getNameOfGoal())){
                return stringStringEntry.getKey();
            }
        }
        throw new RuntimeException("Wasn't able to find the ID of the goal");
    }

    private void sendMoney(GoalUpdater goalUpdater, String goalUId) {
        Request request = new Request.Builder()
                .url(addMoneyUrl(starlingGoalAddMoneyUrl, goalUpdater, goalUId))
                .put(buildRequestBodyForAddingMoneyToGoal(goalUpdater))
                .header("Authorization",
                        "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();

        try {
            String response = client.newCall(request)
                    .execute()
                    .body()
                    .string();

            System.out.println(response);
            JSONObject jsonObject = new JSONObject(response);
            boolean savingsGoalSuccess = jsonObject.getBoolean("success");

            System.out.println("Saved money to goal? = "+savingsGoalSuccess);

        }catch(Exception e)
        {
            log.error("Error in adding money to the goal", e);
        }
    }

    private boolean savingGoalDoesNotExists(GoalUpdater goalUpdater, HashMap<String, String> listOfSavingGoals) {
        boolean doesNotExist = true;

        for (Map.Entry<String, String> stringStringEntry : listOfSavingGoals.entrySet()) {
            if(stringStringEntry.getValue().equalsIgnoreCase(goalUpdater.getNameOfGoal()))
            {
                doesNotExist = false;
                break;
            }
        }
        return doesNotExist;
    }

    public Map<String, String> createGoal(GoalUpdater goalUpdater) {
        //Creating the request
        Request request = new Request.Builder()
                .url(buildPutGoalUrl(starlingGoalUrl, goalUpdater))
                .put(buildRequestBodyForCreatingGoal(goalUpdater))
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
            boolean savingsGoalSuccess = jsonObject.getBoolean("success");
            System.out.println("THE UID OF THE SAVING GOAL");
            return Map.of(jsonObject.getString("savingsGoalUid"), goalUpdater.getNameOfGoal());

        }catch(Exception e)
        {
            log.error("Error in fetching the list of goals", e);
            throw new RuntimeException("Error in fetching goals");
        }
    }

    @SneakyThrows
    private RequestBody buildRequestBodyForAddingMoneyToGoal(GoalUpdater goalUpdater) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(AmountDTO.builder()
                .amount(Amount.builder()
                                .currency("GBP")
                                .minorUnits((int) goalUpdater.getAmountToAdd())
                                .build())
                .build());
        System.out.println("THIS IS THE REQUEST BODY ADDING MONEY TO GOAL");
        System.out.println(requestBody);
        return RequestBody.create(requestBody, MediaType.parse("application/json"));
    }

    @SneakyThrows
    private RequestBody buildRequestBodyForCreatingGoal(GoalUpdater goalUpdater) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(GoalDTO.builder()
                .currency("GBP")
                .name(goalUpdater.getNameOfGoal())
                .base64EncodedPhoto("random")
                .build());
        System.out.println("THIS IS THE REQUEST BODY FOR CREATING A GOAL");
        System.out.println(requestBody);
        return RequestBody.create(requestBody, MediaType.parse("application/json"));
    }

    @SneakyThrows
    private URL addMoneyUrl(String starlingGoalUrl, GoalUpdater goalUpdater, String goalUid) {
        URL finalURL = UriComponentsBuilder.fromHttpUrl(starlingGoalUrl)
                .buildAndExpand(goalUpdater.getAccUId(), goalUid, UUID.randomUUID().toString())
                .toUri()
                .toURL();
        return finalURL;
    }

    @SneakyThrows
    private URL buildPutGoalUrl(String starlingGoalUrl, GoalUpdater goalUpdater) {
        URL finalURL = UriComponentsBuilder.fromHttpUrl(starlingGoalUrl)
                .buildAndExpand(goalUpdater.getAccUId())
                .toUri()
                .toURL();
        return finalURL;
    }

    //Fetches the goals the user currently has
    public HashMap<String, String> fetchCurrentGoalsDetails(String accountUid) {
        log.info("fetchCurrentGoalsDetails:+ fetching the goals");

        HashMap<String, String> mapOfGoals = new HashMap<>();
        List<String> listOfGoals = new ArrayList<>();

        //Create the request
        Request request = new Request.Builder()
                .url(buildURLToFetchGoals(starlingGoalUrl, accountUid))
                .header("Authorization",
                        "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();

        //Fetch
        try {
            String response = client.newCall(request)
                                    .execute()
                                    .body()
                                    .string();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray savingsGoalList = jsonObject.getJSONArray("savingsGoalList");

            for (int i = 0; i < savingsGoalList.length(); i++) {
                String goalName = savingsGoalList.getJSONObject(i).getString("name");
                String goalUId = savingsGoalList.getJSONObject(i).getString("savingsGoalUid");
                mapOfGoals.put(goalUId, goalName);
            }

        }catch(Exception e)
        {
            log.error("Error in fetching the list of goals", e);
        }

        System.out.println("-=- returning " + mapOfGoals.toString());
        return mapOfGoals;

    }

    @SneakyThrows
    private URL buildURLToFetchGoals(String url, String accountUId)
    {
        URL finalURL = UriComponentsBuilder.fromHttpUrl(url)
                .buildAndExpand(accountUId)
                .toUri()
                .toURL();

        log.info("buildURL:- built the URL to gather the transactions={}", finalURL);
        return finalURL;
    }
}
