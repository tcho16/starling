package com.tarikh.interview.starling.domain.ports;

import com.tarikh.interview.starling.integration.exceptions.UnableToRetreiveGoalsException;

import java.util.HashMap;

public interface SavingGoalIdPort {
    HashMap<String, String> getIdsOfSavingGoals(String accountUid) throws UnableToRetreiveGoalsException;
}
