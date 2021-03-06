package com.tarikh.interview.starling.domain.handlers;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RoundUpCalculatorTest {

    RoundUpCalculator roundUpCalculator = new RoundUpCalculator();

    @Test
    public void shouldReturnCorrectAmount()
    {
        int amount = roundUpCalculator.totalNearestPound(List.of(260));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(40);
    }

    @Test
    public void shouldReturnOneIfExactlySpentAPound()
    {
        int amount = roundUpCalculator.totalNearestPound(List.of(200));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(100);
    }

    @Test
    public void shouldReturnZeroIfNoTransactions()
    {
        int amount = roundUpCalculator.totalNearestPound(List.of());

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(0);
    }

    @Test
    public void shouldReturnCorrectAmountFromMultipleTransactions()
    {
        int amount = roundUpCalculator.totalNearestPound(List.of(260,260,260));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(120);
    }

    @Test
    public void shouldReturnCorrectAmountIfPenceIsPassedIn()
    {
        int amount = roundUpCalculator.totalNearestPound(List.of(87));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(13);
    }

    @Test
    public void shouldReturnCorrectAmountIfInLowerSideOfPound()
    {
        int amount = roundUpCalculator.totalNearestPound(List.of(110));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(90);
    }

    @Test
    public void shouldReturnCorrectAmountBasedOnMixedNumbers()
    {
        int amount = roundUpCalculator.totalNearestPound(List.of(435,520,87));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(158);
    }
}