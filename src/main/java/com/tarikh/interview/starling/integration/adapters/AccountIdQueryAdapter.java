package com.tarikh.interview.starling.integration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.AccountDTO;
import com.tarikh.interview.starling.api.AccountsDTO;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.integration.converters.AccountDTOToAccountDetailsConverter;
import com.tarikh.interview.starling.integration.exceptions.NoPrimaryAccountsWereFoundException;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.tarikh.interview.starling.domain.AccountIdQueryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

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
    private final String accessToken;

    @SneakyThrows
    @Override
    public AccountDetails fetchAccountIds(String accountHolderUid) {
        log.info("queryCategoryPort:+ fetching accounts for accountHolderUid={}", accountHolderUid);

        Request request = new Request.Builder()
                .url(starlingAccountUrl)
                .header("Authorization",
                        "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();


            String response = client.newCall(request)
                    .execute()
                    .body()
                    .string();

            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray savingsGoalList = jsonObject.getJSONArray("accounts");

            AccountsDTO accountsDTO = mapper.readValue(response, AccountsDTO.class);
            Optional<AccountDTO> idOfPrimaryAccount = accountsDTO.getAccountDTOList()
                                                                .stream()
                                                                .filter(accountDTO -> accountDTO.accountType.equalsIgnoreCase(accountType))
                                                                .findFirst();

        AccountDTO accountDTO = idOfPrimaryAccount.orElseThrow((Supplier<Throwable>) () ->
                new NoPrimaryAccountsWereFoundException("No Primary account was found for the accountHolderUid = " + accountHolderUid));

        log.info("queryCategoryPort:- fetched accountIDs={}", idOfPrimaryAccount);
            return dtoToAccountDetailsConverter.convert(accountDTO);

    }
}