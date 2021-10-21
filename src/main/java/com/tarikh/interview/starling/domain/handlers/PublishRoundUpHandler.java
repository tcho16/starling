package com.tarikh.interview.starling.domain.handlers;

import static java.time.temporal.ChronoUnit.DAYS;

import com.tarikh.interview.starling.domain.models.AccountDetails;
import org.springframework.stereotype.Component;

import com.tarikh.interview.starling.domain.CategoeryQueryPort;
import com.tarikh.interview.starling.domain.PublishRoundUpPort;
import com.tarikh.interview.starling.domain.TransactionQueryPort;
import com.tarikh.interview.starling.domain.models.TimestampDuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublishRoundUpHandler implements PublishRoundUpPort
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
      //TODO: cover use case for when optional is empty
      Optional<AccountDetails> accountDetails = categoeryQueryPort.queryCategoryPort(timestampDuration.getAccountDetails().getAccountUId());

      //Fetch list of transaction
      calculateWeekDuration(timestampDuration);
      timestampDuration.getAccountDetails().setCategoryId(accountDetails.get().getCategoryId());
      timestampDuration.getAccountDetails().setAccountUId(accountDetails.get().getAccountUId());
      List<Integer> integers = transactionQueryPort.queryForTransactionsBasedOnTimeframe(timestampDuration);



   }

   private void calculateWeekDuration(TimestampDuration timestampDuration)
   {
      timestampDuration.setTimestampEnd(timestampDuration.getTimestampBegin().plus(7, DAYS));
      log.info("calculateWeekDuration:+/- calculated 1 week of transaction={}", timestampDuration);
   }
}
