package com.tarikh.interview.starling.integration.converters;

import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.domain.models.TransactionTimeFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@Slf4j
public class TransactionTimeFrameConverter {

    public TransactionTimeFrame convertToTransactionTimeFrame(AccountDetails accountDetails,
                                                              Instant timestampBegin)
    {
        return TransactionTimeFrame.builder()
                            .timestampBegin(timestampBegin)
                            .timestampEnd(timestampBegin.plus(7, DAYS))
                            .categoryId(accountDetails.getCategoryId())
                            .accountId(accountDetails.getAccountUId())
                            .build();
    }
}
