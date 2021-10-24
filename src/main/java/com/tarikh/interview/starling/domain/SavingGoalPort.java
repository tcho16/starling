package com.tarikh.interview.starling.domain;

import com.tarikh.interview.starling.domain.models.GoalContainer;

public interface SavingGoalPort {

    boolean sendMoneyToGoal(GoalContainer goalContainer);
}
