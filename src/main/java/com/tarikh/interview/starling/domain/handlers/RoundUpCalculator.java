package com.tarikh.interview.starling.domain.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

//This is the logic for caluclating the rounded amount
//We first take the modulus of 100 to get the pence amount
//and then we subtract the pence from 100 (aka £1) to get the amount needed to reach the next £1
//We then divide by 100 since Starling does not accept decimal places.
//We then add the totals and then times 100 to get the value.

@Component
@RequiredArgsConstructor
@Slf4j
public class RoundUpCalculator
{
    public int totalNearestPound(List<Integer> transactions)
    {
        log.info("totalNearestPound:+ totaling the nearest pound for the transactions={}", transactions);
        BigDecimal total = new BigDecimal(0.00);

        for(Integer amount : transactions)
        {
            int penceAmount = amount % 100;
            BigDecimal penceToNearestNextPound = new BigDecimal(100 - penceAmount);
            BigDecimal divide = penceToNearestNextPound.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            total = total.add(divide);
        }

        int roundedAmount = total.multiply(new BigDecimal(100)).intValueExact();
        log.info("totalNearestPound:- rounded amount for the transactions={}", roundedAmount);
        return  roundedAmount;
    }
}
