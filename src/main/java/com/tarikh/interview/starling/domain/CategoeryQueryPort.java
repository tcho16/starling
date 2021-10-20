package com.tarikh.interview.starling.domain;

import com.tarikh.interview.starling.domain.models.AccountDetails;

import java.util.Optional;

public interface CategoeryQueryPort
{
   public Optional<AccountDetails> queryCategoryPort(String accUId);
}
