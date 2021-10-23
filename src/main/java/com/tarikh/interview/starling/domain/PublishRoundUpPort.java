package com.tarikh.interview.starling.domain;

import com.tarikh.interview.starling.domain.models.GoalTimeframe;

public interface PublishRoundUpPort
{
   void publishToGoal(GoalTimeframe goalTimeframe);
}
