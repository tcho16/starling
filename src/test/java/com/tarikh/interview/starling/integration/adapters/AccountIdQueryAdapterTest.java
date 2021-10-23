package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.domain.models.AccountDetails;
import com.tarikh.interview.starling.integration.converters.AccountDTOToAccountDetailsConverter;
import com.tarikh.interview.starling.integration.exceptions.NoPrimaryAccountsWereFoundException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AccountIdQueryAdapterTest {

    private final String dummyToken = "dummyToken";
    private final String accHolderUId = "BOB123";
    private final String accUId = "a8ACCCCCc14-d7b4-4563-9d3d-04f391959cee";
    private final String categoryId = "a8f35c14-d7b4-4563-9d3d-04f391959cee";

    private AccountIdQueryAdapter accountIdQueryAdapter;
    private MockWebServer mockWebServer;
    private AccountDTOToAccountDetailsConverter convert;

    @SneakyThrows
    @BeforeEach
    public void setUp(){
        convert = mock(AccountDTOToAccountDetailsConverter.class);
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String url =  mockWebServer.url("/").toString();
        accountIdQueryAdapter = new AccountIdQueryAdapter(httpClient(), convert, url, dummyToken);
    }

    @AfterEach
     void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void shouldThrowExceptionIfNoPrimaryIdWasRetrieved()
    {
        MockResponse mockedResponse = new MockResponse()
                .setBody(jsonResponseWithNoPrimaryAccounts())
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        assertThrows(NoPrimaryAccountsWereFoundException.class,()->{
            accountIdQueryAdapter.fetchAccountIds(accHolderUId);
        });
    }

    private String jsonResponseWithTwoAccounts()
    {
        return "{\n" +
                "  \"accounts\": [\n" +
                "    {\n" +
                "      \"accountUid\": \"31d945e6-7655-458a-a90a-9db53a491181\",\n" +
                "      \"accountType\": \"PRIMARY\",\n" +
                "      \"defaultCategory\": \"a8f35c14-d7b4-4563-9d3d-04f391959cee\",\n" +
                "      \"currency\": \"GBP\",\n" +
                "      \"createdAt\": \"2021-10-18T21:04:05.309Z\",\n" +
                "      \"name\": \"Personal\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"accountUid\": \"8b7e53a2-56a6-4cfe-909f-093dcc336cb6\",\n" +
                "      \"accountType\": \"ADDITIONAL\",\n" +
                "      \"defaultCategory\": \"c5d62d77-c09d-443b-9d31-97d125b36b18\",\n" +
                "      \"currency\": \"EUR\",\n" +
                "      \"createdAt\": \"2021-10-19T18:11:00.834Z\",\n" +
                "      \"name\": \"Euro\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        //return "{\"accounts\":[{\"accountUid\":\""+ accUId +"\",\"accountType\":\"PRIMARY\",\"defaultCategory\":\""+ categoryId + "\",\"currency\":\"GBP\",\"createdAt\":\"2021-10-18T21:04:05.309Z\",\"name\":\"Personal\"},{\"accountUid\":\"8b7e53a2-56a6-4cfe-909f-093dcc336cb6\",\"accountType\":\"ADDITIONAL\",\"defaultCategory\":\"c5d62d77-c09d-443b-9d31-97d125b36b18\",\"currency\":\"EUR\",\"createdAt\":\"2021-10-19T18:11:00.834Z\",\"name\":\"Euro\"}]}";
    }
    private String jsonResponseWithNoPrimaryAccounts()
    {
        return "{\"accounts\":[{\"accountUid\":\""+ accUId +"\",\"accountType\":\"ADDITIONAL\",\"defaultCategory\":\""+ categoryId + "\",\"currency\":\"GBP\",\"createdAt\":\"2021-10-18T21:04:05.309Z\",\"name\":\"Personal\"},{\"accountUid\":\"8b7e53a2-56a6-4cfe-909f-093dcc336cb6\",\"accountType\":\"ADDITIONAL\",\"defaultCategory\":\"c5d62d77-c09d-443b-9d31-97d125b36b18\",\"currency\":\"EUR\",\"createdAt\":\"2021-10-19T18:11:00.834Z\",\"name\":\"Euro\"}]}";
    }

    private OkHttpClient httpClient()
    {
        return new OkHttpClient.Builder()
                .build();
    }
}