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
    private final SavingGoalCreator creator;
    private final SavingGoalIdFinder goalIdFinder;

    @SneakyThrows
    @Override
    public void sendMoneyToGoal(GoalContainer goalContainer) {
        //Fetch Ids of the goals
        HashMap<String, String> savingGoals = goalIdFinder.getSavingGoals(goalContainer.getAccUId());

        if(savingGoalDoesNotExist(goalContainer.getNameOfGoal(), savingGoals))
        {
            Map<String, String> goal = creator.createGoal(goalContainer);
            goal.entrySet().iterator()
                            .forEachRemaining(newlyCreatedGoal -> savingGoals.put(newlyCreatedGoal.getKey(), newlyCreatedGoal.getValue()));
        }

        String idOfGoal = fetchGoalId(savingGoals, goalContainer.getNameOfGoal());
        sendMoney(goalContainer, idOfGoal);
    }

    private String fetchGoalId(HashMap<String, String> savingGoals, String nameOfGoal) {
        String idOfGoal = "";
        for (Map.Entry<String, String> goal : savingGoals.entrySet()) {
            if(goal.getValue().equalsIgnoreCase(nameOfGoal))
            {
                idOfGoal =  goal.getKey();
                break;
            }
        }

        return idOfGoal;
    }

    private void sendMoney(GoalContainer goalContainer, String idOfGoal) {
        Request request = new Request.Builder()
                .url(addMoneyUrl(starlingGoalAddMoneyUrl, goalContainer, idOfGoal))
                .put(buildRequestBodyForAddingMoneyToGoal(goalContainer))
                .header("Authorization",
                        "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();

        try {
            client.newCall(request)
                    .execute()
                    .body()
                    .string();

        }catch(Exception e)
        {
            log.error("Error in adding money to the goal", e);
            throw new UnableToAddMoneyToGoalException("Was not able to add money to the goal");
        }
    }

    private boolean savingGoalDoesNotExist(String goalName, HashMap<String, String> listOfSavingGoals) {
        boolean doesNotExist = true;

        for (Map.Entry<String, String> stringStringEntry : listOfSavingGoals.entrySet()) {
            if(stringStringEntry.getValue().equalsIgnoreCase(goalName))
            {
                doesNotExist = false;
                break;
            }
        }
        return doesNotExist;
    }

    @SneakyThrows
    private RequestBody buildRequestBodyForAddingMoneyToGoal(GoalContainer goalContainer) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(AmountDTO.builder()
                .amount(Amount.builder()
                                .currency("GBP")
                                .minorUnits((int) goalContainer.getAmountToAdd())
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
