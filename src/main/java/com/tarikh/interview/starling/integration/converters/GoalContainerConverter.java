package com.tarikh.interview.starling.integration.converters;

import com.tarikh.interview.starling.domain.models.GoalContainer;
import org.springframework.stereotype.Component;

@Component
public class GoalContainerConverter {

    public GoalContainer convert(int totalSavedUpFromTransactions, String accountId, String goalName, String goalID) {
        return GoalContainer.builder()
                .goalId(goalID)
                .accUId(accountId)
                .nameOfGoal(goalName)
                .amountToAdd(totalSavedUpFromTransactions)
                .build();
    }
}
