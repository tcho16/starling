package com.tarikh.interview.starling.domain.handlers;

import com.tarikh.interview.starling.domain.*;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.domain.models.GoalContainer;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;
import com.tarikh.interview.starling.domain.models.TransactionTimeFrame;
import com.tarikh.interview.starling.integration.adapters.SavingGoalCreator;
import com.tarikh.interview.starling.integration.adapters.SavingGoalIdFinder;
import com.tarikh.interview.starling.integration.converters.GoalContainerConverter;
import com.tarikh.interview.starling.integration.converters.TransactionTimeFrameConverter;
import com.tarikh.interview.starling.integration.exceptions.UnableToRetreiveGoalsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublishRoundUpHandlerTest {

    @Mock
    private AccountIdQueryPort accountIdQueryPort;
    @Mock
    private TransactionQueryPort transactionQueryPort;
    @Mock
    private SavingGoalPort savingGoalPort;
    @Mock
    private RoundUpCalculator roundUpCalculator;
    @Mock
    private SavingGoalIdPort savingGoalIdFinder;
    @Mock
    private SavingGoalCreatorPort creator;
    @Mock
    private TransactionTimeFrameConverter timeFrameConverter;
    @Mock
    private GoalContainerConverter goalContainerConverter;
    @InjectMocks
    PublishRoundUpHandler publishRoundUpHandler;

    private final String goalName = "Fitness";
    private final String goalId = "Fitness123";
    private final String accountHolderId = "BOB123";
    private final String accountUId = "678BOB";
    private final String categoryId = "45Category";
    private final Instant instant = Instant.now();

    @Test
    public void shouldReturnSuccessfulResponseUponDepositingMoney() throws UnableToRetreiveGoalsException {
        when(accountIdQueryPort.fetchAccountIds(accountHolderId)).thenReturn(createAccountDetails());
        when(timeFrameConverter.convertToTransactionTimeFrame(any(AccountDetails.class), any(Instant.class))).thenCallRealMethod();
        when(transactionQueryPort.queryTransactionAmountsBasedOnTimeframe(any(TransactionTimeFrame.class))).thenReturn(listOfTransactions(1,2,3));
        when(roundUpCalculator.totalNearestPound(any())).thenReturn(10);
        when(savingGoalIdFinder.getIdsOfSavingGoals(anyString())).thenReturn(createMapOfGoals());

        when(goalContainerConverter.convert(anyInt(), anyString(), anyString(), anyString())).thenReturn(buildGoalContainer());

        when(savingGoalPort.sendMoneyToGoal(any(GoalContainer.class))).thenReturn(true);

        String responseMessage = publishRoundUpHandler.publishToGoal(createGoalTimeframe(goalName));
        assertThat(responseMessage).as("The response of the saving to the goal")
                                    .isNotEmpty()
                                    .contains("Successfuly sent rounded figure");
    }

    @Test
    public void shouldCreateGoalIfCurrentGoalDoesNotExist() throws UnableToRetreiveGoalsException {
        when(accountIdQueryPort.fetchAccountIds(accountHolderId)).thenReturn(createAccountDetails());
        when(timeFrameConverter.convertToTransactionTimeFrame(any(AccountDetails.class), any(Instant.class))).thenCallRealMethod();
        when(transactionQueryPort.queryTransactionAmountsBasedOnTimeframe(any(TransactionTimeFrame.class))).thenReturn(listOfTransactions(1,2,3));
        when(roundUpCalculator.totalNearestPound(any())).thenReturn(10);
        when(savingGoalIdFinder.getIdsOfSavingGoals(anyString())).thenReturn(createMapOfGoals());

        when(goalContainerConverter.convert(anyInt(), anyString(), anyString(), anyString())).thenReturn(buildGoalContainer());

        when(savingGoalPort.sendMoneyToGoal(any(GoalContainer.class))).thenReturn(true);

        String responseMessage = publishRoundUpHandler.publishToGoal(createGoalTimeframe("brand new goal"));
        assertThat(responseMessage).as("The response of the saving to the goal")
                .isNotEmpty()
                .contains("Successfuly sent rounded figure");

        verify(creator).createGoal(anyString(), anyString());
    }

    @Test
    public void shouldIndicateThatFailingToSaveMoneyToGoalFailed() throws UnableToRetreiveGoalsException {
        when(accountIdQueryPort.fetchAccountIds(accountHolderId)).thenReturn(createAccountDetails());
        when(timeFrameConverter.convertToTransactionTimeFrame(any(AccountDetails.class), any(Instant.class))).thenCallRealMethod();
        when(transactionQueryPort.queryTransactionAmountsBasedOnTimeframe(any(TransactionTimeFrame.class))).thenReturn(listOfTransactions(1,2,3));
        when(roundUpCalculator.totalNearestPound(any())).thenReturn(10);
        when(savingGoalIdFinder.getIdsOfSavingGoals(anyString())).thenReturn(createMapOfGoals());

        when(goalContainerConverter.convert(anyInt(), anyString(), anyString(), anyString())).thenReturn(buildGoalContainer());

        when(savingGoalPort.sendMoneyToGoal(any(GoalContainer.class))).thenReturn(false);

        String responseMessage = publishRoundUpHandler.publishToGoal(createGoalTimeframe("brand new goal"));
        assertThat(responseMessage).as("The response of the saving to the goal")
                .isNotEmpty()
                .contains("Did not send money to goal");

        verify(creator).createGoal(anyString(), anyString());
    }

    private GoalContainer buildGoalContainer() {
        return GoalContainer.builder()
                            .nameOfGoal(goalName)
                            .accUId(accountUId)
                            .goalId(goalId)
                            .amountToAdd(5)
                            .build();
    }

    private HashMap<String, String> createMapOfGoals() {
        HashMap<String, String> map = new HashMap<>();
        map.put(goalId, goalName);
        return map;
    }

    private List<Integer> listOfTransactions(Integer ... transactions)
    {
        return Arrays.asList(transactions);
    }

    private AccountDetails createAccountDetails()
    {
        return AccountDetails.builder()
                            .accountUId(accountUId)
                            .categoryId(categoryId)
                            .accountHolderUId(accountHolderId)
                            .build();
    }

    private GoalTimeframe createGoalTimeframe(String goalName)
    {
        return GoalTimeframe.builder()
                .goalName(goalName)
                .accountHolderId(accountHolderId)
                .timestampBegin(instant)
                .build();
    }
}