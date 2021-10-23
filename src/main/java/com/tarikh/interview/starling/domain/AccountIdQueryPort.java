package com.tarikh.interview.starling.domain;

import com.tarikh.interview.starling.domain.models.AccountDetails;

public interface AccountIdQueryPort
{
   public AccountDetails fetchAccountIds(String accUId);
}
