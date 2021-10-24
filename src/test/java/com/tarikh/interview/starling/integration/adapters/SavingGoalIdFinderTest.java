package com.tarikh.interview.starling.integration.adapters;

import com.tarikh.interview.starling.integration.exceptions.UnableToRetreiveGoalsException;
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
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SavingGoalIdFinderTest {

    private SavingGoalIdFinder savingGoalIdFinder;
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
        savingGoalIdFinder = new SavingGoalIdFinder(accessToken, url, httpClient());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void shouldRetrieveGoals() throws UnableToRetreiveGoalsException {

        MockResponse mockedResponse = new MockResponse()
                .setBody(responseWithGoals())
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        HashMap<String, String> savingGoals = savingGoalIdFinder.getSavingGoals(accIID);

        assertThat(savingGoals).as("The map containing the users goals")
                .isNotEmpty()
                .contains(MapEntry.entry(goalId, nameOfGoal));
    }

    @Test
    public void shouldNotThrowErrorIfNoGoalsPresent() throws UnableToRetreiveGoalsException {

        MockResponse mockedResponse = new MockResponse()
                .setBody(responseWithNoGoals())
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        HashMap<String, String> savingGoals = savingGoalIdFinder.getSavingGoals(accIID);

        assertThat(savingGoals).as("The map containing the users goals")
                .isEmpty();
    }

    private String responseWithNoGoals()
    {
        return "{\n" +
                "  \"savingsGoalList\": [\n" +
                "    \n" +
                "  ]\n" +
                "}";
    }

    private String responseWithGoals()
    {
        return "{\n" +
                "  \"savingsGoalList\": [\n" +
                "    {\n" +
                "      \"savingsGoalUid\": \""+ goalId+"\",\n" +
                "      \"name\": \""+ nameOfGoal +"\",\n" +
                "      \"totalSaved\": {\n" +
                "        \"currency\": \"GBP\",\n" +
                "        \"minorUnits\": 13254\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    private OkHttpClient httpClient()
    {
        return new OkHttpClient.Builder()
                .build();
    }

}