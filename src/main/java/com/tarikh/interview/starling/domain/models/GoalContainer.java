package com.tarikh.interview.starling.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Data
public class GoalContainer {
    private final double amountToAdd;
    private final String accUId;
    private final String nameOfGoal;
    private final String goalId;
}
