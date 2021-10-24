package com.tarikh.interview.starling.integration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.AccountDTO;
import com.tarikh.interview.starling.api.AccountsDTO;
import com.tarikh.interview.starling.domain.AccountIdQueryPort;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.integration.converters.AccountDTOToAccountDetailsConverter;
import com.tarikh.interview.starling.integration.exceptions.AccessTokenExpiredException;
import com.tarikh.interview.starling.integration.exceptions.NoPrimaryAccountsWereFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

//This adapter is responsible for fetching the accountID and categoryId based on a PRIMARY account.
//If no primary account is present then an exception is thrown which is caught by the global exception handler
//Since this is the first API call to Starling's API, we ensure that the response code is not FORBIDDEN,
//if it is FORBIDDEN then we throw a custom exception.

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountIdQueryAdapter implements AccountIdQueryPort {

    private final String accountType = "PRIMARY";
    private final OkHttpClient client;
    private final AccountDTOToAccountDetailsConverter dtoToAccountDetailsConverter;
    private final String starlingAccountUrl;
    private final ObjectMapper mapper;
    private final String accessToken;

    @Override
    public AccountDetails fetchAccountIds(String accountHolderUId) {
        log.info("fetchAccountIds:+ fetching accounts for accountHolderUid={}", accountHolderUId);

        Request request = buildRequest();
        String responseJson = null;
        AccountsDTO accountsDTO = new AccountsDTO();
        try {
            Response response = client.newCall(request)
                    .execute();
            if (response.code() == HttpStatus.FORBIDDEN.value()) {
                throw new AccessTokenExpiredException("Access Token has been expired. Generate a new one");
            }
            responseJson = response.body().string();
            accountsDTO = mapper.readValue(responseJson, AccountsDTO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Optional<AccountDTO> idOfPrimaryAccount = accountsDTO.getAccountDTOList()
                .stream()
                .filter(accountDTO -> accountDTO.accountType.equalsIgnoreCase(accountType))
                .findFirst();

        AccountDTO accountDTO = idOfPrimaryAccount.orElseThrow(() ->
                new NoPrimaryAccountsWereFoundException("No Primary account was found for the accountHolderUid = " + accountHolderUId));

        log.info("fetchAccountIds:- fetched accountIDs={}", idOfPrimaryAccount);
        return dtoToAccountDetailsConverter.convert(accountDTO);
    }

    @NotNull
    private Request buildRequest() {
        return new Request.Builder()
                .url(starlingAccountUrl)
                .header("Authorization",
                        "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();
    }
}