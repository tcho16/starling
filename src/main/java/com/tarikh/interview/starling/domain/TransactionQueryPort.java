package com.tarikh.interview.starling.domain;

import java.util.List;

import com.tarikh.interview.starling.domain.models.TimestampDuration;
import com.tarikh.interview.starling.domain.models.Transaction;

public interface TransactionQueryPort
{
   List<Transaction> queryForTransactionsBasedOnTimeframe(TimestampDuration timestampDuration);
}
