package com.tarikh.interview.starling.domain.handlers;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RoundUpCalculatorTest {

    RoundUpCalculator roundUpCalculator = new RoundUpCalculator();

    @Test
    public void shouldReturnCorrectAmount()
    {
        double amount = roundUpCalculator.totalNearestPound(List.of(260));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(0.40);
    }

    //TODO: Clarify assumption
    @Test
    public void shouldReturnOneIfExactlySpentAPound()
    {
        double amount = roundUpCalculator.totalNearestPound(List.of(200));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(1.00);
    }

    @Test
    public void shouldReturnZeroIfNoTransactions()
    {
        double amount = roundUpCalculator.totalNearestPound(List.of());

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(0.00);
    }

    @Test
    public void shouldReturnCorrectAmountFromMultipleTransactions()
    {
        double amount = roundUpCalculator.totalNearestPound(List.of(260,260,260));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(1.20);
    }

    @Test
    public void shouldReturnCorrectAmountIfPenceIsPassedIn()
    {
        double amount = roundUpCalculator.totalNearestPound(List.of(87));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(0.13);
    }

    @Test
    public void shouldReturnCorrectAmountIfInLowerSideOfPound()
    {
        double amount = roundUpCalculator.totalNearestPound(List.of(110));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(0.90);
    }

    @Test
    public void shouldReturnCorrectAmountBasedOnMixedNumbers()
    {
        double amount = roundUpCalculator.totalNearestPound(List.of(435,520,87));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(1.58);
    }
}