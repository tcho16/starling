package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.domain.models.AccountDetails;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CategoeryQueryAdapterTest {

    private final String dummyToken = "dummyToken";
    private final String accHolderUId = "BOB123";
    private final String accUId = "a8ACCCCCc14-d7b4-4563-9d3d-04f391959cee";
    private final String categoryId = "a8f35c14-d7b4-4563-9d3d-04f391959cee";

    private CategoeryQueryAdapter categoeryQueryAdapter;
    private MockWebServer mockWebServer;

    @SneakyThrows
    @BeforeEach
    public void setUp(){
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String url =  mockWebServer.url("/").toString();

        categoeryQueryAdapter = new CategoeryQueryAdapter(httpClient(), url, dummyToken);
    }

    @AfterEach
     void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @SneakyThrows
    @Test
    public void shouldReturnCategoryIdForPrimaryAccount()
    {
        MockResponse mockedResponse = new MockResponse()
                .setBody(jsonResponseWithTwoAccounts())
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        AccountDetails categoryId = categoeryQueryAdapter.queryCategoryPort(accHolderUId).get();

        assertThat(categoryId).as("The category id for the primary account")
                .isNotNull()
                .hasFieldOrPropertyWithValue("categoryId",categoryId)
                .hasFieldOrPropertyWithValue("accountUId", accUId);
    }

    @Test
    public void shouldReturnEmptyObjectIfNoCategoryIdWasFound()
    {
        MockResponse mockedResponse = new MockResponse()
                .setBody(jsonResponseWithNoPrimaryAccounts())
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        Optional<AccountDetails> categoryId = categoeryQueryAdapter.queryCategoryPort(accHolderUId);

        assertThat(categoryId).as("The category id for the primary account")
                .isEmpty();
    }

    private String jsonResponseWithTwoAccounts()
    {
        return "{\"accounts\":[{\"accountUid\":\"31d945e6-7655-458a-a90a-9db53a491181\",\"accountType\":\"PRIMARY\",\"defaultCategory\":\""+ categoryId + "\",\"currency\":\"GBP\",\"createdAt\":\"2021-10-18T21:04:05.309Z\",\"name\":\"Personal\"},{\"accountUid\":\"8b7e53a2-56a6-4cfe-909f-093dcc336cb6\",\"accountType\":\"ADDITIONAL\",\"defaultCategory\":\"c5d62d77-c09d-443b-9d31-97d125b36b18\",\"currency\":\"EUR\",\"createdAt\":\"2021-10-19T18:11:00.834Z\",\"name\":\"Euro\"}]}";
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