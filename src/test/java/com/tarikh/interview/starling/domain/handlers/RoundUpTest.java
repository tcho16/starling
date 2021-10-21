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
        BigDecimal amount = roundUp.nearestPoundTotaler(List.of(260));

        assertThat(amount.doubleValue()).as("The rounded up amount to the next pound")
                .isEqualTo(0.40);
    }

    @Test
    public void shouldReturnZeroIfNoTransactions()
    {
        BigDecimal amount = roundUp.nearestPoundTotaler(List.of());

        assertThat(amount.doubleValue()).as("The rounded up amount to the next pound")
                .isEqualTo(0.00);
    }

    @Test
    public void shouldReturnCorrectAmountFromMultipleTransactions()
    {
        BigDecimal amount = roundUp.nearestPoundTotaler(List.of(260,260,260));

        assertThat(amount.doubleValue()).as("The rounded up amount to the next pound")
                .isEqualTo(1.20);
    }
}