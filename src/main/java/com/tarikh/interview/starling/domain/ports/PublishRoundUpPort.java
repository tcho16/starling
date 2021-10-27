package com.tarikh.interview.starling.domain.ports;

import com.tarikh.interview.starling.domain.models.GoalTimeframe;

public interface PublishRoundUpPort
{
   void publishToGoal(GoalTimeframe goalTimeframe);
}
