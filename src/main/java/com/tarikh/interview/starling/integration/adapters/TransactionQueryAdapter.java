package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.domain.TransactionQueryPort;
import com.tarikh.interview.starling.domain.models.TimestampDuration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionQueryAdapter implements TransactionQueryPort {

    private final String accessToken;
    private final String starlingTransactionUrl;
    private final OkHttpClient client;

    @Override
    public List<Integer> queryTransactionAmountsBasedOnTimeframe(TimestampDuration timestampDuration) {

        log.info("queryForTransactionsBasedOnTimeframe:+ calling the transaction query endpoint");

        Request request = buildRequest(timestampDuration);
        List<Integer> transactions = new ArrayList<>();

        try {
            String response = client.newCall(request)
                    .execute()
                    .body()
                    .string();

            //TODO: Refactor this
            JSONObject jsonObject = new JSONObject(response);
            JSONArray feedItems = jsonObject.getJSONArray("feedItems");

            for(int i = 0; i < feedItems.length(); i++)
            {
                Integer amount = feedItems.getJSONObject(i).getJSONObject("amount").getInt("minorUnits");

                transactions.add(amount);
            }

           return transactions;

        } catch (IOException e) {
            System.out.println("in the exception!");
            e.printStackTrace();
            return List.of();
        }
    }

    //TODO: Extract authToken
    private Request buildRequest(TimestampDuration timestampDuration) {
        return new Request.Builder()
                .url(buildURL(starlingTransactionUrl, timestampDuration))
                .header("Authorization",
                        "Bearer " + accessToken)
                .build();
    }

    @SneakyThrows
    private URL buildURL(String url, TimestampDuration timestampDuration) {
        log.info("buildURL:+ building the URL from the object={}", timestampDuration);

        URL finalURL = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("minTransactionTimestamp={minTime}")
                .queryParam("maxTransactionTimestamp={maxTime}")
                .buildAndExpand(
                        timestampDuration.getAccountDetails().getAccountUId(),
                        timestampDuration.getAccountDetails().getCategoryId(),
                        timestampDuration.getTimestampBegin().toString(),
                        timestampDuration.getTimestampEnd().toString())
                .toUri()
                .toURL();

        log.info("buildURL:- built the URL to gather the transactions={}", finalURL);
        return finalURL;
    }
}
