package com.tarikh.interview.starling.domain.ports;

import java.util.Map;

public interface SavingGoalCreatorPort {
    Map<String, String> createGoal(String accUId, String nameOfGoal);
}
