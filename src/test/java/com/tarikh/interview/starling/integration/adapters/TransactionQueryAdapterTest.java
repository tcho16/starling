package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.domain.models.TransactionTimeFrame;
import com.tarikh.interview.starling.integration.exceptions.UnableToRetrieveTransactionException;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TransactionQueryAdapterTest {

    private final String url = "dummyURL";
    private final String accessToken = "dummyToken";
    private TransactionQueryAdapter transactionQueryAdapter;
    private MockWebServer mockWebServer;

    @SneakyThrows
    @BeforeEach
    public void setUp(){
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String url =  mockWebServer.url("/").toString();
        transactionQueryAdapter = new TransactionQueryAdapter(accessToken, url, httpClient());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void shouldNotThrowErrorIfTransactionResponseIsEmpty() throws UnableToRetrieveTransactionException {
        MockResponse mockedResponse = new MockResponse()
                .setBody(emptyTransaction())
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        List<Integer> integers = transactionQueryAdapter.queryTransactionAmountsBasedOnTimeframe(buildTransactionTimeframe());

        assertThat(integers).as("The list containing the transactions")
                .isEmpty();
    }

    @Test
    public void shouldThrowErrorIfUnableToRetrieveTransaction() throws UnableToRetrieveTransactionException {
        MockResponse mockedResponse = new MockResponse()
                .setBody(malformedResponse())
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        assertThrows(UnableToRetrieveTransactionException.class,()->{
            transactionQueryAdapter.queryTransactionAmountsBasedOnTimeframe(buildTransactionTimeframe());
        });
    }

    @Test
    public void shouldFetchTransactionsFromResponse() throws UnableToRetrieveTransactionException {
        MockResponse mockedResponse = new MockResponse()
                .setBody(jsonResponseContaining5200And5100OfTransactions())
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        List<Integer> integers = transactionQueryAdapter.queryTransactionAmountsBasedOnTimeframe(buildTransactionTimeframe());

        assertThat(integers).as("The list containing the transactions")
                .containsExactly(5200, 5100);
    }

    private TransactionTimeFrame buildTransactionTimeframe()
    {
        Instant now = Instant.now();
        return TransactionTimeFrame.builder()
                .accountId("BOB123")
                .categoryId("123BOB")
                .timestampBegin(now)
                .timestampEnd(now.plus(5, DAYS))
                .build();

    }

    private String malformedResponse()
    {
        return "THIS IS AN INCORRECT JSON FORMAT";
    }

    private String emptyTransaction()
    {
        return "{\"feedItems\":[]}";
    }

    private String jsonResponseContaining5200And5100OfTransactions()
    {
        return "{\n" +
                "  \"feedItems\": [\n" +
                "    {\n" +
                "      \"feedItemUid\": \"aab97a64-bc40-4153-816c-293e293c38de\",\n" +
                "      \"categoryUid\": \"a8f35c14-d7b4-4563-9d3d-04f391959cee\",\n" +
                "      \"amount\": {\n" +
                "        \"currency\": \"GBP\",\n" +
                "        \"minorUnits\": 5200\n" +
                "      },\n" +
                "      \"sourceAmount\": {\n" +
                "        \"currency\": \"GBP\",\n" +
                "        \"minorUnits\": 5200\n" +
                "      },\n" +
                "      \"direction\": \"OUT\",\n" +
                "      \"updatedAt\": \"2021-10-23T20:02:39.976Z\",\n" +
                "      \"transactionTime\": \"2021-10-23T20:02:39.804Z\",\n" +
                "      \"settlementTime\": \"2021-10-23T20:02:39.804Z\",\n" +
                "      \"source\": \"INTERNAL_TRANSFER\",\n" +
                "      \"status\": \"SETTLED\",\n" +
                "      \"transactingApplicationUserUid\": \"0d0ff27b-0ba2-4d17-8dae-7c632e163371\",\n" +
                "      \"counterPartyType\": \"CATEGORY\",\n" +
                "      \"counterPartyUid\": \"176946f1-367d-4f02-a3c7-8a1a0ace1761\",\n" +
                "      \"counterPartyName\": \"testingThis\",\n" +
                "      \"country\": \"GB\",\n" +
                "      \"spendingCategory\": \"SAVING\",\n" +
                "      \"hasAttachment\": false,\n" +
                "      \"hasReceipt\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"feedItemUid\": \"c94eef92-3c8e-4eb4-ba50-2cff2e6ea4b1\",\n" +
                "      \"categoryUid\": \"a8f35c14-d7b4-4563-9d3d-04f391959cee\",\n" +
                "      \"amount\": {\n" +
                "        \"currency\": \"GBP\",\n" +
                "        \"minorUnits\": 5100\n" +
                "      },\n" +
                "      \"sourceAmount\": {\n" +
                "        \"currency\": \"GBP\",\n" +
                "        \"minorUnits\": 5100\n" +
                "      },\n" +
                "      \"direction\": \"OUT\",\n" +
                "      \"updatedAt\": \"2021-10-23T19:57:31.348Z\",\n" +
                "      \"transactionTime\": \"2021-10-23T19:57:31.279Z\",\n" +
                "      \"settlementTime\": \"2021-10-23T19:57:31.279Z\",\n" +
                "      \"source\": \"INTERNAL_TRANSFER\",\n" +
                "      \"status\": \"SETTLED\",\n" +
                "      \"transactingApplicationUserUid\": \"0d0ff27b-0ba2-4d17-8dae-7c632e163371\",\n" +
                "      \"counterPartyType\": \"CATEGORY\",\n" +
                "      \"counterPartyUid\": \"176946f1-367d-4f02-a3c7-8a1a0ace1761\",\n" +
                "      \"counterPartyName\": \"testingThis\",\n" +
                "      \"country\": \"GB\",\n" +
                "      \"spendingCategory\": \"SAVING\",\n" +
                "      \"hasAttachment\": false,\n" +
                "      \"hasReceipt\": false\n" +
                "    }\n" +
                "  ]\n" +
                "}";}


    private OkHttpClient httpClient()
    {
        return new OkHttpClient.Builder()
                .build();
    }

}