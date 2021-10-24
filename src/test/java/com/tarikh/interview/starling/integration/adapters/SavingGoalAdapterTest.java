package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.domain.models.GoalContainer;
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
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavingGoalAdapterTest {

    private MockWebServer mockWebServer;
    private String accId = "AccountIBOB123";
    private final String accessToken = "dummyToken";
    private SavingGoalAdapter savingGoalAdapter;

    @SneakyThrows
    @BeforeEach
    public void setUp(){
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String url =  mockWebServer.url("/").toString();
        savingGoalAdapter = new SavingGoalAdapter(accessToken, url, httpClient());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @SneakyThrows
    @Test
    public void shouldNotThrowErrorsWhenSuccessfullySendMoneyToGoal()
    {
        MockResponse mockedResponse = new MockResponse()
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        GoalContainer goal = createGoalContainer("Bobs New Goal");

        assertDoesNotThrow(() ->
           savingGoalAdapter.sendMoneyToGoal(goal));
    }

    @SneakyThrows
    @Test
    public void shouldIndicateSuccessfullySentMoneyToGoal()
    {
        MockResponse mockedResponse = new MockResponse()
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        GoalContainer goal = createGoalContainer("Bobs New Goal");

        boolean isSuccess = savingGoalAdapter.sendMoneyToGoal(goal);

        assertThat(isSuccess).as("indication whether the money was sent")
                .isTrue();
    }

    @SneakyThrows
    @Test
    public void shouldIndicateMoneyWasNotDeposited()
    {
        MockResponse mockedResponse = new MockResponse()
                .setResponseCode(500)
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        GoalContainer goal = createGoalContainer("Bobs goal");

        boolean isSuccess = savingGoalAdapter.sendMoneyToGoal(goal);

        assertThat(isSuccess).isFalse();
    }

    private GoalContainer createGoalContainer(String nameOfGoal) {
        return GoalContainer.builder()
                .accUId(accId)
                .amountToAdd(123.1)
                .nameOfGoal(nameOfGoal)
                .build();
    }

    private OkHttpClient httpClient()
    {
        return new OkHttpClient.Builder()
                .build();
    }

}