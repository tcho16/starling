package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.domain.models.GoalContainer;
import com.tarikh.interview.starling.integration.exceptions.UnableToCreateGoalException;
import com.tarikh.interview.starling.integration.exceptions.UnableToRetrieveTransactionException;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SavingGoalCreatorTest {

    private SavingGoalCreator savingGoalCreator;
    private MockWebServer mockWebServer;
    private String accessToken = "accessToken";
    private String goalId = "123";
    private String nameOfGoal = "Bobs goal";
    private String accIID = "789";

    @SneakyThrows
    @BeforeEach
    public void setUp(){
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String url =  mockWebServer.url("/").toString();
        savingGoalCreator = new SavingGoalCreator(accessToken, url, httpClient());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void shouldSuccessfullyCreateGoal()
    {
        MockResponse mockedResponse = new MockResponse()
                .setBody(successfulResponse())
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        Map<String, String> goal = savingGoalCreator.createGoal(accIID, nameOfGoal);

        assertThat(goal).as("The map containing the ID of the newly created goal")
                .isNotEmpty()
                .containsExactly(MapEntry.entry(goalId, nameOfGoal));
    }

    @Test
    public void shouldThrowErrorIfUnableToCreateGoal()
    {
        MockResponse mockedResponse = new MockResponse()
                .setBody(malformedResponse())
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        assertThrows(UnableToCreateGoalException.class,()->{
            savingGoalCreator.createGoal(accIID, nameOfGoal);
        });
    }

    private String malformedResponse()
    {
        return "MALFORMED JSON RESPOSNE";
    }

    private String successfulResponse()
    {
        return "{\n" +
                "  \"savingsGoalUid\": \"" + goalId+ "\",\n" +
                "  \"success\": true,\n" +
                "  \"errors\": []\n" +
                "}";
    }


    private OkHttpClient httpClient()
    {
        return new OkHttpClient.Builder()
                .build();
    }
}