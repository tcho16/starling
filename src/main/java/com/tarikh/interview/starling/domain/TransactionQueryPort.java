package com.tarikh.interview.starling.domain;

import java.util.List;

import com.tarikh.interview.starling.domain.models.TimestampDuration;

public interface TransactionQueryPort
{
   List<Integer> queryForTransactionsBasedOnTimeframe(TimestampDuration timestampDuration);
}
