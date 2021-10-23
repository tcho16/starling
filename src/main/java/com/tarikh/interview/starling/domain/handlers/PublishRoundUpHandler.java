package com.tarikh.interview.starling.domain.handlers;

import static java.time.temporal.ChronoUnit.DAYS;

import com.tarikh.interview.starling.domain.*;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.domain.models.GoalContainer;
import com.tarikh.interview.starling.domain.models.TransactionTimeFrame;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import com.tarikh.interview.starling.domain.models.GoalTimeframe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublishRoundUpHandler implements PublishRoundUpPort
{
   private final AccountIdQueryPort accountIdQueryPort;
   private final TransactionQueryPort transactionQueryPort;
   private final SavingGoalPort savingGoalPort;
   private final RoundUpCalculator roundUpCalculator;

   @SneakyThrows
   @Override
   public void publishToGoal(GoalTimeframe goalTimeframe)
   {
      log.info("publishToGoal:+ goal={}", goalTimeframe);

      //Fetch the categoryId and accountId for the given accountHolderUId
      AccountDetails accountDetails = accountIdQueryPort.fetchAccountIds(goalTimeframe.getAccountHolderId());

      //Building a domain object so we can use it in the adapters
      TransactionTimeFrame transactionTimeFrame = buildTransactionTimeFrame(accountDetails, goalTimeframe.getTimestampBegin());

      //Fetch transactions from between date
      List<Integer> integers = transactionQueryPort.queryTransactionAmountsBasedOnTimeframe(transactionTimeFrame);

      //Calculating the nearest rounded amount to insert into goal
      double totalSavedUpFromTransactions = roundUpCalculator.totalNearestPound(integers);

      //Create a goalContainer to be used in the adapters
      GoalContainer goalContainer = buildGoalUpdater(totalSavedUpFromTransactions, accountDetails.getAccountUId(), goalTimeframe.getGoalName());

      //Call the saving goal adapter to persist what we have done
      savingGoalPort.sendMoneyToGoal(goalContainer);
      log.info("publishToGoal:- goal has been updated with rounded amount.");
   }

   private GoalContainer buildGoalUpdater( double totalSavedUpFromTransactions, String accountId, String goalName) {
      return GoalContainer.builder()
              .amountToAdd(totalSavedUpFromTransactions)
              .nameOfGoal(goalName)
              .accUId(accountId)
              .build();
   }

   private TransactionTimeFrame buildTransactionTimeFrame(AccountDetails accountDetails, Instant timestampBegin) {
      return TransactionTimeFrame.builder()
              .categoryId(accountDetails.getCategoryId())
              .accountId(accountDetails.getAccountUId())
              .timestampBegin(timestampBegin)
              .timestampEnd(timestampBegin.plus(7, DAYS))
              .build();
   }
}
