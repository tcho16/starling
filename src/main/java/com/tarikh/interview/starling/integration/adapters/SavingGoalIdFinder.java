package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.integration.exceptions.UnableToRetreiveGoalsException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class SavingGoalIdFinder{

    private final String accessToken;
    private final String starlingGoalUrl;
    private final OkHttpClient client;

    public HashMap<String, String> getSavingGoals(String accountUid) throws UnableToRetreiveGoalsException {
        HashMap<String, String> mapOfGoals = new HashMap<>();

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
            throw new UnableToRetreiveGoalsException("Unable to retreive goals for account " + accountUid);
        }

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