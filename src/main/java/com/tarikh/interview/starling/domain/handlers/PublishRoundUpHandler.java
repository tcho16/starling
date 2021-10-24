package com.tarikh.interview.starling.domain.handlers;

import com.tarikh.interview.starling.domain.AccountIdQueryPort;
import com.tarikh.interview.starling.domain.PublishRoundUpPort;
import com.tarikh.interview.starling.domain.SavingGoalPort;
import com.tarikh.interview.starling.domain.TransactionQueryPort;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.domain.models.GoalContainer;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;
import com.tarikh.interview.starling.domain.models.TransactionTimeFrame;
import com.tarikh.interview.starling.integration.adapters.SavingGoalCreator;
import com.tarikh.interview.starling.integration.adapters.SavingGoalIdFinder;
import com.tarikh.interview.starling.integration.converters.GoalContainerConverter;
import com.tarikh.interview.starling.integration.converters.TransactionTimeFrameConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublishRoundUpHandler implements PublishRoundUpPort {
    private final AccountIdQueryPort accountIdQueryPort;
    private final TransactionQueryPort transactionQueryPort;
    private final SavingGoalPort savingGoalPort;
    private final RoundUpCalculator roundUpCalculator;
    private final SavingGoalIdFinder savingGoalIdFinder;
    private final SavingGoalCreator creator;
    private final TransactionTimeFrameConverter timeFrameConverter;
    private final GoalContainerConverter goalContainerConverter;

    @SneakyThrows
    @Override
    public void publishToGoal(GoalTimeframe goalTimeframe) {
        log.info("publishToGoal:+ goal={}", goalTimeframe);

        //Fetch the categoryId and accountId for the given accountHolderUId
        AccountDetails accountDetails = accountIdQueryPort.fetchAccountIds(goalTimeframe.getAccountHolderId());

        //Building a domain object so we can use it in the adapters
        TransactionTimeFrame transactionTimeFrame = timeFrameConverter.convertToTransactionTimeFrame(accountDetails, goalTimeframe.getTimestampBegin());

        //Fetch transactions from between dates
        List<Integer> transactions = transactionQueryPort.queryTransactionAmountsBasedOnTimeframe(transactionTimeFrame);

        //Calculating the nearest rounded amount to insert into goal
        double totalSavedUpFromTransactions = roundUpCalculator.totalNearestPound(transactions);
        System.out.println("the rounded up total = " + totalSavedUpFromTransactions);

        //Fetch the IDs of the saving goal
        String savingGoalID = getSavingGoalId(accountDetails.getAccountUId(), goalTimeframe.getGoalName());

        //Create a goalContainer object to be used in the saving adapter adapters
        GoalContainer goalContainer = goalContainerConverter.convert(totalSavedUpFromTransactions,
                                                                    accountDetails.getAccountUId(),
                                                                    goalTimeframe.getGoalName(),
                                                                    savingGoalID);

        //Call the saving goal adapter to persist the amount
        savingGoalPort.sendMoneyToGoal(goalContainer);
        log.info("publishToGoal:- goal has been updated with rounded amount.");
    }

    @SneakyThrows
    private String getSavingGoalId(String accountUId, String goalName) {
        HashMap<String, String> mapOfCurrentGoals = savingGoalIdFinder.getSavingGoals(accountUId);

        if (savingGoalDoesNotExist(goalName, mapOfCurrentGoals)) {
            Map<String, String> newlyCreatedGoal = creator.createGoal(accountUId, goalName);
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