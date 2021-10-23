package com.tarikh.interview.starling.integration.converters;

import com.tarikh.interview.starling.api.AccountDTO;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountDTOToAccountDetailsConverter implements Converter<AccountDTO, AccountDetails> {

    @Override
    public AccountDetails convert(AccountDTO source) {
        return AccountDetails.builder()
                .categoryId(source.categoryId)
                .accountUId(source.accountUId)
                .build();
    }
}
