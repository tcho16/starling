package com.tarikh.interview.starling.domain;

import com.tarikh.interview.starling.domain.models.TimestampDuration;

public interface RoundUpPort
{
   public void publishToGoal(TimestampDuration timestampDuration);
}
