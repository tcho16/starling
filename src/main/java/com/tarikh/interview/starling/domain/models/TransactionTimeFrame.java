package com.tarikh.interview.starling.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Data
@Slf4j
@Builder
public class TransactionTimeFrame {
    private String accountId;
    private String categoryId;
    private Instant timestampBegin;
    private Instant timestampEnd;
}
