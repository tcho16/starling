package com.tarikh.interview.starling.domain.handlers;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.WEEKS;

import org.springframework.stereotype.Component;

import com.tarikh.interview.starling.domain.CategoeryQueryPort;
import com.tarikh.interview.starling.domain.RoundUpPort;
import com.tarikh.interview.starling.domain.TransactionQueryPort;
import com.tarikh.interview.starling.domain.models.TimestampDuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoundUpHandler implements RoundUpPort
{
   //LOGIC
   //Create Payload containing timestamp
   //Fetch the list of transactions from: /api/v2/feed/account/{accountUid}/category/{categoryUid}/transactions-between
   //Once fetched the list of transaction, Fetch the list of goals and check if your goal is created
   //if goal doesn't exist, create one else input money into that goal
   private final CategoeryQueryPort categoeryQueryPort;
   private final TransactionQueryPort transactionQueryPort;

   @Override
   public void publishToGoal(TimestampDuration timestampDuration)
   {
      log.info("publishToGoal:+ object recieved={}", timestampDuration);

      //Fetch the category for the given accUId
      String categoryID = categoeryQueryPort.queryCategoryPort(timestampDuration.getAccountDetails().getAccountUId());

      //Fetch list of transaction
      calculateWeekDuration(timestampDuration);
      timestampDuration.getAccountDetails().setCategoryId(categoryID);
      transactionQueryPort.queryForTransactionsBasedOnTimeframe(timestampDuration);

   }

   private void calculateWeekDuration(TimestampDuration timestampDuration)
   {
      timestampDuration.setTimestampEnd(timestampDuration.getTimestampBegin().plus(7, DAYS));
      log.info("calculateWeekDuration:- timestamptDuration={}", timestampDuration);
   }
}
