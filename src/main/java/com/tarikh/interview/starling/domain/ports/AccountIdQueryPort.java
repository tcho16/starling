package com.tarikh.interview.starling.domain.ports;

import com.tarikh.interview.starling.domain.models.AccountDetails;

public interface AccountIdQueryPort
{
   AccountDetails fetchAccountIds(String accountHolderUId);
}
