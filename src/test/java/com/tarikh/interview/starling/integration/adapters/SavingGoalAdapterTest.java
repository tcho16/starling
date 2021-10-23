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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavingGoalAdapterTest {

    private MockWebServer mockWebServer;
    private String accId = "AccountIBOB123";
    private final String url = "dummyURL";
    private final String accessToken = "dummyToken";
    private SavingGoalAdapter savingGoalAdapter;
    private SavingGoalCreator creator;
    private SavingGoalIdFinder goalIdFinder;

    @SneakyThrows
    @BeforeEach
    public void setUp(){
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String url =  mockWebServer.url("/").toString();
        creator = mock(SavingGoalCreator.class);
        goalIdFinder = mock(SavingGoalIdFinder.class);
        savingGoalAdapter = new SavingGoalAdapter(accessToken, url, httpClient(), creator, goalIdFinder);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void shouldCreateGoalIfGoalDoesNotAlreadyExists()
    {
        MockResponse mockedResponse = new MockResponse()
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        when(goalIdFinder.getSavingGoals(anyString())).thenReturn(idsOfGoals());

        GoalContainer goal = createGoalContainer("Bobs New Goal");

        savingGoalAdapter.sendMoneyToGoal(goal);

        verify(creator).createGoal(any(GoalContainer.class));
    }

    @Test
    public void shouldNotNeedToCreateGoalIfGoalWithSameNameAlreadyExists()
    {
        MockResponse mockedResponse = new MockResponse()
                .addHeader("Content-Type", "application/json");

        mockWebServer.enqueue(mockedResponse);

        when(goalIdFinder.getSavingGoals(anyString())).thenReturn(idsOfGoals());

        GoalContainer goal = createGoalContainer("Bobs goal");

        savingGoalAdapter.sendMoneyToGoal(goal);

        verify(creator, never()).createGoal(any(GoalContainer.class));
    }

    private GoalContainer createGoalContainer(String nameOfGoal) {
        return GoalContainer.builder()
                .accUId(accId)
                .amountToAdd(123.1)
                .nameOfGoal(nameOfGoal)
                .build();
    }

    private HashMap<String, String> idsOfGoals()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("123", "Bobs goal");
        return map;
    }


    private OkHttpClient httpClient()
    {
        return new OkHttpClient.Builder()
                .build();
    }

}