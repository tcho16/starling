package com.tarikh.interview.starling.domain;

import com.tarikh.interview.starling.domain.models.GoalTimeframe;

public interface PublishRoundUpPort
{
   String publishToGoal(GoalTimeframe goalTimeframe);
}
