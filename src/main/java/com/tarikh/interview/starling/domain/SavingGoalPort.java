package com.tarikh.interview.starling.domain;

import com.tarikh.interview.starling.domain.models.GoalUpdater;

public interface SavingGoalPort {
//    void create();
//
//    void fetchGoals();

    void sendMoneyToGoal(GoalUpdater goalUpdater);
}
