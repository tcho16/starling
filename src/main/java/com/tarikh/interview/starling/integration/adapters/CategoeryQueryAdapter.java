package com.tarikh.interview.starling.integration.adapters;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.AccountsDTO;
import com.tarikh.interview.starling.domain.models.AccountDetails;
import org.springframework.stereotype.Component;

import com.tarikh.interview.starling.domain.CategoeryQueryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Component
@Slf4j
@RequiredArgsConstructor
public class CategoeryQueryAdapter implements CategoeryQueryPort {

    private final OkHttpClient client;
    private final String starlingAccountUrl;
    private final String accessToken;

    @Override
    public Optional<AccountDetails> queryCategoryPort(String accUId) {
        log.info("queryCategoryPort:+ fetching accounts for accUId={}", accUId);
        //TODO: Extract request
        Request request = new Request.Builder()
                .url(starlingAccountUrl)
                .header("Authorization",
                        "Bearer " + accessToken)
                .header("Accept", "application/json")
                .build();

        try {
            String response = client.newCall(request)
                    .execute()
                    .body()
                    .string();

            ObjectMapper mapper = new ObjectMapper();
            AccountsDTO accountsDTO = mapper.readValue(response, AccountsDTO.class);
            Optional<AccountDetails> primaryCategoryID = accountsDTO.getAccountDTOList()
                                                                .stream()
                                                                .filter(accountDTO -> accountDTO.accountType.equalsIgnoreCase("PRIMARY"))
                                                                .findFirst()
                                                                .map(accountDTO -> AccountDetails.builder().accountUId(accountDTO.accountUId).categoryId(accountDTO.categoryId).build());

            log.info("queryCategoryPort:- fetched categoryId={}", primaryCategoryID);
            return primaryCategoryID;
        } catch (IOException e) {
            log.error("queryCategoryPort:- UNSUCESSFUL={}", e);
            return Optional.empty();
        }
    }
}