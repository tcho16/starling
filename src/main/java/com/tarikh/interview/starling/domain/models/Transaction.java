package com.tarikh.interview.starling.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
public class Transaction
{
private int transactionAmount;
}
