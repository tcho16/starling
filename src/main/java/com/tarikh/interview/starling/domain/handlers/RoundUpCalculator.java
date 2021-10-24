package com.tarikh.interview.starling.domain.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoundUpCalculator
{
    public int totalNearestPound(List<Integer> transactions)
    {
        BigDecimal total = new BigDecimal(0.00);

        for(Integer amount : transactions)
        {
            int penceAmount = amount % 100;
            BigDecimal penceToNearestNextPound = new BigDecimal(100 - penceAmount);
            BigDecimal divide = penceToNearestNextPound.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            total = total.add(divide);
        }

        return total.multiply(new BigDecimal(100)).intValueExact();
    }
}
