package com.tarikh.interview.starling.domain.handlers;

import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.domain.models.GoalContainer;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;
import com.tarikh.interview.starling.domain.models.TransactionTimeFrame;
import com.tarikh.interview.starling.domain.ports.*;
import com.tarikh.interview.starling.integration.converters.GoalContainerConverter;
import com.tarikh.interview.starling.integration.converters.TransactionTimeFrameConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This is the class that orchestrates the calls to make the logic happen
//It has collaborators injected such as the Ports and Converters
//It first fetches the IDs of the primary account
//Then fetches the transaction
//Then performs the business logic in computing the rounded value
//Then the fetches the ID of the goal
//It then persists the rounded value to the goal.

@Component
@RequiredArgsConstructor
@Slf4j
public class PublishRoundUpHandler implements PublishRoundUpPort {
    private final AccountIdQueryPort accountIdQueryPort;
    private final TransactionQueryPort transactionQueryPort;
    private final SavingGoalPort savingGoalPort;
    private final RoundUpCalculator roundUpCalculator;
    private final SavingGoalIdPort savingGoalIdPort;
    private final SavingGoalCreatorPort savingGoalCreatorPort;
    private final TransactionTimeFrameConverter timeFrameConverter;
    private final GoalContainerConverter goalContainerConverter;

    @SneakyThrows
    @Override
    public boolean publishToGoal(GoalTimeframe goalTimeframe) {
        log.info("publishToGoal:+ goal={}", goalTimeframe);

        //Fetch the categoryId and accountId for the given accountHolderUId
        AccountDetails accountDetails = accountIdQueryPort.fetchAccountIds(goalTimeframe.getAccountHolderId());

        //Building a domain object so we can use it in the adapters.
        //This is to make the boundaries loosely coupled/anti corruption layer.
        TransactionTimeFrame transactionTimeFrame = timeFrameConverter.convertToTransactionTimeFrame(accountDetails, goalTimeframe.getTimestampBegin());

        //Fetch transactions from between dates
        List<Integer> transactions = transactionQueryPort.queryTransactionAmountsBasedOnTimeframe(transactionTimeFrame);

        //Calculating the nearest rounded amount to insert into goal
        int totalSavedUpFromTransactions = roundUpCalculator.totalNearestPound(transactions);

        //Fetch the IDs of the saving goal
        String savingGoalID = getSavingGoalId(accountDetails.getAccountUId(), goalTimeframe.getGoalName());

        //Create a goalContainer object to be used in the saving goal adapters
        //This is for ensure the domain/integration is losely coupled.
        GoalContainer goalContainer = goalContainerConverter.convert(totalSavedUpFromTransactions,
                accountDetails.getAccountUId(),
                goalTimeframe.getGoalName(),
                savingGoalID);

        //Call the saving goal adapter to persist the amount
        boolean isSuccessful = savingGoalPort.sendMoneyToGoal(goalContainer);

        log.info("publishToGoal:-");
        return isSuccessful;
    }

    //This method gets the ID of the saving goal the user wants to deposit into
    //If the goal does not exist then it creates it and then returns the ID of the newly created goal
    //Else it returns the ID of the existing goal
    @SneakyThrows
    private String getSavingGoalId(String accountUId, String goalName) {
        HashMap<String, String> mapOfCurrentGoals = savingGoalIdPort.getIdsOfSavingGoals(accountUId);

        if (savingGoalDoesNotExist(goalName, mapOfCurrentGoals)) {
            Map<String, String> newlyCreatedGoal = savingGoalCreatorPort.createGoal(accountUId, goalName);
            return fetchGoalId(newlyCreatedGoal, goalName);
        } else {
            return fetchGoalId(mapOfCurrentGoals, goalName);
        }

    }

    private String fetchGoalId(Map<String, String> savingGoals, String nameOfGoal) {
        String idOfGoal = "";
        for (Map.Entry<String, String> goal : savingGoals.entrySet()) {
            if (goal.getValue().equalsIgnoreCase(nameOfGoal)) {
                idOfGoal = goal.getKey();
                break;
            }
        }
        return idOfGoal;
    }

    private boolean savingGoalDoesNotExist(String goalName, HashMap<String, String> listOfSavingGoals) {
        boolean doesNotExist = true;

        for (Map.Entry<String, String> stringStringEntry : listOfSavingGoals.entrySet()) {
            if (stringStringEntry.getValue().equalsIgnoreCase(goalName)) {
                doesNotExist = false;
                break;
            }
        }
        return doesNotExist;
    }
}