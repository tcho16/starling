package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.domain.TransactionQueryPort;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;
import com.tarikh.interview.starling.domain.models.TransactionTimeFrame;
import com.tarikh.interview.starling.integration.exceptions.UnableToRetrieveTransactionException;
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
    public List<Integer> queryTransactionAmountsBasedOnTimeframe(TransactionTimeFrame timeFrame) throws UnableToRetrieveTransactionException {

        log.info("queryForTransactionsBasedOnTimeframe:+ calling the transaction query endpoint");

        Request request = buildRequest(timeFrame);
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

        } catch (Exception e) {
            throw new UnableToRetrieveTransactionException("Error retrieving transaction for accountId=" + timeFrame.getAccountId());
        }
    }

    private Request buildRequest(TransactionTimeFrame timeFrame) {
        return new Request.Builder()
                .url(buildURL(starlingTransactionUrl, timeFrame))
                .header("Authorization",
                        "Bearer " + accessToken)
                .build();
    }

    @SneakyThrows
    private URL buildURL(String url, TransactionTimeFrame timeFrame) {
        log.info("buildURL:+ building the URL from the object={}", timeFrame);

        URL finalURL = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("minTransactionTimestamp={minTime}")
                .queryParam("maxTransactionTimestamp={maxTime}")
                .buildAndExpand(
                        timeFrame.getAccountId(),
                        timeFrame.getCategoryId(),
                        timeFrame.getTimestampBegin().toString(),
                        timeFrame.getTimestampEnd().toString())
                .toUri()
                .toURL();

        log.info("buildURL:- built the URL to gather the transactions={}", finalURL);
        return finalURL;
    }
}
