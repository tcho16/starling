package com.tarikh.interview.starling.domain;

import java.util.Map;

public interface SavingGoalCreatorPort {
    Map<String, String> createGoal(String accUId, String nameOfGoal);
}
