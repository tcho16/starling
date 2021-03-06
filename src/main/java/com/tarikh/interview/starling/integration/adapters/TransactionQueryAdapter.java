package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.domain.ports.TransactionQueryPort;
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

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//This is the Transaction adapter.
//We fetch the list of transactions based on the timestamps.
//If no transactions are recorded, then we throw an exception indicating that no transactions were found

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionQueryAdapter implements TransactionQueryPort {

    private final String accessToken;
    private final String starlingTransactionUrl;
    private final OkHttpClient client;

    @Override
    public List<Integer> queryTransactionAmountsBasedOnTimeframe(TransactionTimeFrame timeFrame) throws UnableToRetrieveTransactionException {

        log.info("queryForTransactionsBasedOnTimeframe:+ fetching transactions");

        Request request = buildRequest(timeFrame);

        List<Integer> transactions = fetchTransactions(timeFrame, request);

        if (transactions.size() == 0) {
            throw new UnableToRetrieveTransactionException("No transactions were found from the provided date");
        }

        log.info("queryForTransactionsBasedOnTimeframe:-");
        return transactions;
    }

    private List<Integer> fetchTransactions(TransactionTimeFrame timeFrame, Request request) {
        try {
            String response = client.newCall(request)
                    .execute()
                    .body()
                    .string();

            JSONObject jsonObject = new JSONObject(response);
            JSONArray feedItems = jsonObject.getJSONArray("feedItems");

            return IntStream.range(0, feedItems.length())
                    .mapToObj(counter -> feedItems.getJSONObject(counter).getJSONObject("amount").getInt("minorUnits"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Unable to retreive the transactions for the account provided", e);
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

        return finalURL;
    }
}
