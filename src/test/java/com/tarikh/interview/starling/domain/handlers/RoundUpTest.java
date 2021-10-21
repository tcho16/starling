package com.tarikh.interview.starling.domain.handlers;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RoundUpTest {

    RoundUp roundUp = new RoundUp();

    @Test
    public void shouldReturnCorrectAmount()
    {
        double amount = roundUp.nearestPoundTotaler(List.of(260));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(0.40);
    }

    //TODO: Clarify assumption
    @Test
    public void shouldReturnOneIfExactlySpentAPound()
    {
        double amount = roundUp.nearestPoundTotaler(List.of(200));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(1.00);
    }

    @Test
    public void shouldReturnZeroIfNoTransactions()
    {
        double amount = roundUp.nearestPoundTotaler(List.of());

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(0.00);
    }

    @Test
    public void shouldReturnCorrectAmountFromMultipleTransactions()
    {
        double amount = roundUp.nearestPoundTotaler(List.of(260,260,260));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(1.20);
    }

    @Test
    public void shouldReturnCorrectAmountIfPenceIsPassedIn()
    {
        double amount = roundUp.nearestPoundTotaler(List.of(87));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(0.13);
    }

    @Test
    public void shouldReturnCorrectAmountIfInLowerSideOfPound()
    {
        double amount = roundUp.nearestPoundTotaler(List.of(110));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(0.90);
    }

    @Test
    public void shouldReturnCorrectAmountBasedOnMixedNumbers()
    {
        double amount = roundUp.nearestPoundTotaler(List.of(435,520,87));

        assertThat(amount).as("The rounded up amount to the next pound")
                .isEqualTo(1.58);
    }
}