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
                        "Bearer eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_31SSY7bMBD8SqDz9ED7dsstH8gDWmTLJkyRAknNZBDk76FEyrIcIzdWVS_Vzf6dCGuTPsFZAKdJv1uHRgp1GVDd3pmekrfELoOPSHk6jnkzQDpgDiXPGmg5EjSsLnLK6qJoMh9Mv-ak96hsqzQvu7dEoAtEUxftSiBjelHuh5aczE_Bfe2hzcaOdzm0XUFQspLD0NU18JSNREXDK9752k7fSMUMPno7ZQ4jr2ooi7yCDocMfFZaM6Kc5YPP8GN9Z4ysDVlpUXVD1ZbQtNhASTUCtlUJWVljXubZMDb5OjDTM61LCU6BSW2J94aQf9u562YfFE70UnBf85MgOCknRkHmzEth3YmJgHPjjffEhbuDoDiH7DrRPfLAn0Y4-oaLu2ojrP9GEIqLD8EXlCF4QImKRWsMDQemlTNahkYrEzWtRmEmdEIr0COMi-L2Ltl79x2E1myxTk_7iDShiIUleSPq0uM8y6872qImVBwd9Zwk-RI7jJq5kVsHmQ2NZMh7t_-Tgo2gzRIZ-Q04uphtjsfEf8WYSoZdcZ9uIofeDfbMw02NeBtqxi-iXQogDhHAEQRiwkucKWjHE5xBZZEdDj0NwyJv_f6TdFBHt4CPhgHvBdZ78Lc1CXfUlJr5O3iosBGg14N4ZmOW0aOQu_0wz4naogwxErM7AXuWwnItfvgPs3DRh48TF6c5cVudRyYszH_5qxKH-KLWIYai7Ep8kcQhLizS5JwfcJkjdLhdK2jDH3qe2b3ZmX2RD_pT3XlH2zcx-_FMzXyM1DJYZvwK1-vYuzxyW9TjCW2_9HxTyZ-_w7e03-sFAAA.umZRAYXMHZN-qvAxpRL8UH9A-vlSK4_HiyVK03mkmALcRZheG2qtQNnGNtvIxDSEHccE9Z0bwOgV_QgqC-rbhuHPVt5z-ROLxJ2WCVYCV5JlD-94NH60Zhnck7Moz4rKtw-DDDD-zEjtzf6PpehvfGII0qRx3chZ9vjy_ZstERT_GrnZvCRN_R1zjGAdyfc9OvgHyzc2zlbuT-CCY5dVHKQJfhfhESno5kxRTvaJ6eQjzE6tQtowcjOPX0pg7xwBly1f5NsaXgZgbvUP5IVonoXaie7puCOPtghj0h4ynClpH3TZeuSxf5oyL7Ud2B8ODNHbbtmarbbVKkOw30uzsdlwFsZTgg289zGPnZb55PkHuZ6PrHs6b6BU6TS2jCp_NaawbSQ7RUmx4y3hHlW5zpC8V-uxXmG_JPcOsTDSna-MPfp3Z3VYDvGHbfKWPVVfchvK2pT0pfoMLQ0xdaO9TtsCapVyyMrISLNvSVNVc4CO6ycOzZwzLl2JPLrSCv2ThoS6384f9WCBsnlyBhbihW4eu_VM4u4XvLp2by20mmhIXd2NUMkirXlR4rjI40DLLRbM-4YId-2c7Jg3bg9dvBB9lEyErsWC-Hzn00My8Ij34M8ZF5mVIMduHRtE45J_vsgOuMjushI8iQBFagvhvS88UsNmXOzpbu1K_f8IiS4").header("Accept", "application/json")
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
