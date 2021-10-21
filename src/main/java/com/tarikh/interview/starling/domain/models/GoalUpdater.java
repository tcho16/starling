package com.tarikh.interview.starling.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
@Builder
@Data
public class GoalUpdater {
    private double amountToAdd;
    private String accUId;
    private String nameOfGoal;
}
