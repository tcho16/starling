package com.tarikh.interview.starling.domain.ports;

import com.tarikh.interview.starling.domain.models.GoalContainer;

public interface SavingGoalPort {

    void sendMoneyToGoal(GoalContainer goalContainer);
}
