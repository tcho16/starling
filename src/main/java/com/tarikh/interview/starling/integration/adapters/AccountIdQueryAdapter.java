package com.tarikh.interview.starling.integration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.AccountDTO;
import com.tarikh.interview.starling.api.AccountsDTO;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.integration.converters.AccountDTOToAccountDetailsConverter;
import com.tarikh.interview.starling.integration.exceptions.AccessTokenExpiredException;
import com.tarikh.interview.starling.integration.exceptions.NoPrimaryAccountsWereFoundException;
import lombok.SneakyThrows;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.tarikh.interview.starling.domain.AccountIdQueryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

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
    public AccountDetails fetchAccountIds(String accountHolderUid) {
        log.info("queryCategoryPort:+ fetching accounts for accountHolderUid={}", accountHolderUid);

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
                new NoPrimaryAccountsWereFoundException("No Primary account was found for the accountHolderUid = " + accountHolderUid));

        log.info("queryCategoryPort:- fetched accountIDs={}", idOfPrimaryAccount);
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