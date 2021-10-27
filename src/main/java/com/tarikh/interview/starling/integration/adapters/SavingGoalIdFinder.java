package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.domain.ports.SavingGoalIdPort;
import com.tarikh.interview.starling.integration.exceptions.UnableToRetreiveGoalsException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.stream.IntStream;


//This adapter is responsible for fetching the goals associated by the user.
//It then returns a Map<String, String> where the key is the goal ID and the value is the name of the goal.
@Component
@Slf4j
@RequiredArgsConstructor
public class SavingGoalIdFinder implements SavingGoalIdPort {

    private final String accessToken;
    private final String starlingGoalUrl;
    private final OkHttpClient client;

    public HashMap<String, String> getIdsOfSavingGoals(String accountUid) throws UnableToRetreiveGoalsException {
        //Create the request
        Request request = buildRequest(accountUid);

        //Fetch
        return fetchGoalDetails(accountUid, request);
    }

    private HashMap<String, String> fetchGoalDetails(String accountUid, Request request) throws UnableToRetreiveGoalsException {
        HashMap<String, String> mapOfGoals = new HashMap<>();

        try {
            String response = fetchGoals(request);

            JSONObject jsonObject = new JSONObject(response);
            JSONArray savingsGoalList = jsonObject.getJSONArray("savingsGoalList");

            IntStream.range(0, savingsGoalList.length())
                    .forEach(counter -> {
                        String goalName = savingsGoalList.getJSONObject(counter).getString("name");
                        String goalUId = savingsGoalList.getJSONObject(counter).getString("savingsGoalUid");
                        mapOfGoals.put(goalUId, goalName);
                    });

        } catch (Exception e) {
            log.error("Error in fetching the list of goals", e);
            throw new UnableToRetreiveGoalsException("Unable to retreive goals for account " + accountUid);
        }
        return mapOfGoals;
    }

    @SneakyThrows
    @NotNull
    private String fetchGoals(Request request) {
        try {
            return client.newCall(request)
                    .execute()
                    .body()
                    .string();
        } catch (IOException e) {
            throw new UnableToRetreiveGoalsException("Error in calling the service to fetch goals");
        }
    }

    @NotNull
    private Request buildRequest(String accountUid) {
        return new Request.Builder()
                .url(buildURLToFetchGoals(starlingGoalUrl, accountUid))
                .header("Authorization",
                        "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();
    }

    @SneakyThrows
    private URL buildURLToFetchGoals(String url, String accountUId) {
        URL finalURL = UriComponentsBuilder.fromHttpUrl(url)
                .buildAndExpand(accountUId)
                .toUri()
                .toURL();
        return finalURL;
    }
}
