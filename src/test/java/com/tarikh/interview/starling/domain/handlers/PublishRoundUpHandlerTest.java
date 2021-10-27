package com.tarikh.interview.starling.domain.handlers;

import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.domain.models.GoalContainer;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;
import com.tarikh.interview.starling.domain.models.TransactionTimeFrame;
import com.tarikh.interview.starling.domain.ports.*;
import com.tarikh.interview.starling.integration.converters.GoalContainerConverter;
import com.tarikh.interview.starling.integration.converters.TransactionTimeFrameConverter;
import com.tarikh.interview.starling.integration.exceptions.NoPrimaryAccountsWereFoundException;
import com.tarikh.interview.starling.integration.exceptions.UnableToRetreiveGoalsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    public void shouldNotThrowErrorIfSuccessfulUponDepositingMoney() throws UnableToRetreiveGoalsException {
        when(accountIdQueryPort.fetchAccountIds(accountHolderId)).thenReturn(createAccountDetails());
        when(timeFrameConverter.convertToTransactionTimeFrame(any(AccountDetails.class), any(Instant.class))).thenCallRealMethod();
        when(transactionQueryPort.queryTransactionAmountsBasedOnTimeframe(any(TransactionTimeFrame.class))).thenReturn(listOfTransactions(1,2,3));
        when(roundUpCalculator.totalNearestPound(any())).thenReturn(10);
        when(savingGoalIdFinder.getIdsOfSavingGoals(anyString())).thenReturn(mapOfGoalDetails());

        when(goalContainerConverter.convert(anyInt(), anyString(), anyString(), anyString())).thenReturn(buildGoalContainer());

        doNothing().when(savingGoalPort).sendMoneyToGoal(any(GoalContainer.class));

        assertDoesNotThrow(() -> {publishRoundUpHandler.publishToGoal(createGoalTimeframe(goalName));});
    }

    @Test
    public void shouldCreateGoalIfCurrentGoalDoesNotExist() throws UnableToRetreiveGoalsException {
        when(accountIdQueryPort.fetchAccountIds(accountHolderId)).thenReturn(createAccountDetails());
        when(timeFrameConverter.convertToTransactionTimeFrame(any(AccountDetails.class), any(Instant.class))).thenCallRealMethod();
        when(transactionQueryPort.queryTransactionAmountsBasedOnTimeframe(any(TransactionTimeFrame.class))).thenReturn(listOfTransactions(1,2,3));
        when(roundUpCalculator.totalNearestPound(any())).thenReturn(10);
        when(savingGoalIdFinder.getIdsOfSavingGoals(anyString())).thenReturn(mapOfGoalDetails());

        when(goalContainerConverter.convert(anyInt(), anyString(), anyString(), anyString())).thenReturn(buildGoalContainer());

        doNothing().when(savingGoalPort).sendMoneyToGoal(any(GoalContainer.class));

        assertDoesNotThrow(() -> {publishRoundUpHandler.publishToGoal(createGoalTimeframe("goalName"));});

        verify(creator).createGoal(anyString(), anyString());
    }

    @Test
    public void shouldNotCreateGoalIfCurrentGoalDoesExist() throws UnableToRetreiveGoalsException {
        when(accountIdQueryPort.fetchAccountIds(accountHolderId)).thenReturn(createAccountDetails());
        when(timeFrameConverter.convertToTransactionTimeFrame(any(AccountDetails.class), any(Instant.class))).thenCallRealMethod();
        when(transactionQueryPort.queryTransactionAmountsBasedOnTimeframe(any(TransactionTimeFrame.class))).thenReturn(listOfTransactions(1,2,3));
        when(roundUpCalculator.totalNearestPound(any())).thenReturn(10);
        when(savingGoalIdFinder.getIdsOfSavingGoals(anyString())).thenReturn(mapOfGoalDetails());

        when(goalContainerConverter.convert(anyInt(), anyString(), anyString(), anyString())).thenReturn(buildGoalContainer());

        doNothing().when(savingGoalPort).sendMoneyToGoal(any(GoalContainer.class));

        assertDoesNotThrow(() -> {publishRoundUpHandler.publishToGoal(createGoalTimeframe(goalName));});

        verify(creator, times(0)).createGoal(anyString(), anyString());
    }

    @Test
    public void shouldIndicateThatSavingMoneyToGoalFailedWithAnException() throws UnableToRetreiveGoalsException {
        when(accountIdQueryPort.fetchAccountIds(accountHolderId)).thenReturn(createAccountDetails());
        when(timeFrameConverter.convertToTransactionTimeFrame(any(AccountDetails.class), any(Instant.class))).thenCallRealMethod();
        when(transactionQueryPort.queryTransactionAmountsBasedOnTimeframe(any(TransactionTimeFrame.class))).thenReturn(listOfTransactions(1,2,3));
        when(roundUpCalculator.totalNearestPound(any())).thenReturn(10);
        when(savingGoalIdFinder.getIdsOfSavingGoals(anyString())).thenReturn(mapOfGoalDetails());
        when(goalContainerConverter.convert(anyInt(), anyString(), anyString(), anyString())).thenReturn(buildGoalContainer());

        doThrow(NoPrimaryAccountsWereFoundException.class).when(savingGoalPort).sendMoneyToGoal(any());

        assertThrows(NoPrimaryAccountsWereFoundException.class, () ->  publishRoundUpHandler.publishToGoal(createGoalTimeframe("brand new goal")));

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

    private HashMap<String, String> mapOfGoalDetails() {
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