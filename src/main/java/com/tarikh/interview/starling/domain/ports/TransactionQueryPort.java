package com.tarikh.interview.starling.domain.ports;

import java.util.List;

import com.tarikh.interview.starling.domain.models.GoalTimeframe;
import com.tarikh.interview.starling.domain.models.TransactionTimeFrame;
import com.tarikh.interview.starling.integration.exceptions.UnableToRetrieveTransactionException;

public interface TransactionQueryPort
{
   List<Integer> queryTransactionAmountsBasedOnTimeframe(TransactionTimeFrame timeFrame) throws UnableToRetrieveTransactionException;
}
