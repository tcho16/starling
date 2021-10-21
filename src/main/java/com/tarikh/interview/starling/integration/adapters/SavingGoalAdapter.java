package com.tarikh.interview.starling.integration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.GoalDTO;
import com.tarikh.interview.starling.domain.SavingGoalPort;
import com.tarikh.interview.starling.domain.models.GoalUpdater;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URL;

@Component
@Slf4j
@RequiredArgsConstructor
public class SavingGoalAdapter implements SavingGoalPort {

    private final String starlingGoalAddMoneyUrl;
    private final String starlingGoalUrl;
    private final OkHttpClient client;

    public void create(GoalUpdater goalUpdater) {

        //Creating the request
        buildRequestBody(goalUpdater);
        Request request = new Request.Builder()
                .url(buildPutGoalUrl(starlingGoalUrl, goalUpdater))
                .put(buildRequestBody(goalUpdater))
                .header("Authorization",
                        "Bearer eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_31SSY7bMBD8ykDn4UCbtd3mlg_kAS12yyZMkQJJeTII8vdQImVZjpEbq6q3avbvRFibdAlMgiGN-sM6MFKocw_q-sH1mLwndu59RIrpMOR1z9IeclZiVrMGgVjNqyKnrCqKOvPB9GtKOo_KtqjzsnlPBLhANKe0WQjgXM_K_dASyfwU6Gv3TTa02OasaQtiJS-R9W1VMUz5QFTUeMLW13b6Sipk8KKF6uQzIMuQlUMJDNoqZTm0RVr1BccWfIa39ck5WRuy0uLU9qemZHUDNSup8lnNqWRZWUFe5lk_1PlimOuJlqWESRmX2hJ2hgDfNu6yjs8UjPRScN_TkyCQlBODIHPkpbDuwESAaPzgHaFwdxAU54BfRrpH7vjLCEdvMLuLNsL6b2RCobgJnEGG4B4kKB5H42CQca2c0TI0WpioaTUIM4ITWjE9sGFWaO-SvXffQGjNZ-v0uFmkEUQsLMkPos4dTJP8vqM1agSF4KhDkuRLbDBq5kpuMTIZGsiQn93-TwpjBG2SwMlvwNHZrD4eE_8VYyoZfoHN3UgO_DTQcQ9XNeLV1ATfRJsUQDQRwB7ExAjn6Clo-5M5A8oC3yf0NOtnee22n6Sd2rsFvDcMeCuw3IO_rVG4vabU3N_BQ4WVYHo5iGc2Zhk9CLmNH_wcqDXKECcxuQOwRyks18LNf5hlZ73PceCimwO31nlkwsL8l78qsYsvau1iKMovhLMkZHFhkSbnvMF5itDBeq1MG3zoeWS3Zkf2RT7TX-rOO1q_idvbMzXhEKm5t9z4FS7XsXV55NaoxxNaf-n5ppI_fwHdUwWL6wUAAA.aukHoE6d9eODqHJrsTJfkx0nzJm9j2k4fmwW8fYGTN6gB0FjScb31p-qPoH194vGWyi6IEdATJ8SF0X83x0Ew9VqItzEzYyGJ-fGRPslGxzfgvURAB83VT5OlahoZZCw1MrmrjaT5I1_gsEtXDlWrXaFbctgJid6yMTuh4jd98ZzJkB8RkdgB2LswIXB5pvMREmEKgkcycXPEPBdR0ot-BgSfeMbf6GaeqWHQaUrKZg-zdCVLyZIMsuG_NXo5LMhKa4Un2Q4fNmYDnsIYa7Mp6k71SWXKb2V4d54bnBDmDwhuc2pw3MwlySCYZERZP6Ipl6SAhOeBo8bYt17XJDg2ZTgA_myXGWTgalK8z214M0UH4pRwDOYloyovx5WGelo-0vAcZ_9Bi7TxmSPvFZVHKP36Uc_icCgrLSvcX4YDkEL3wrpZzH1_Oe6YxD6vf06OKdnwwWAGs03xy20gFL48e3O3UV3jVHDE4FgRmgh2fi6CBQ5b7XTh9IheJSK78xb1nDe6s5gWTEzGn5-oCh8_r-4TN-08jG5DdWhnAzGn24VMaYqGC33UrOeAo9xvmG8mdF1tSC37akyhNGhmq1AXR9PHnvexWHvkQnZEvUt2Mkar__lJ5jCRDt8zau8spj3xrRj77PzHmXQzxQoVMhPiJ-zJiPTQvxr0GFrxi3cNms")
                .header("Accept", "application/json")
                .build();

        try {
            String response = client.newCall(request)
                    .execute()
                    .
                    .body()
                    .string();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SneakyThrows
    private RequestBody buildRequestBody(GoalUpdater goalUpdater) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(GoalDTO.builder()
                .currency("GBP")
                .name(goalUpdater.getNameOfGoal())
                .base64EncodedPhoto("random")
                .build());
        System.out.println("THIS IS THE REQUEST BODY FOR CREATING A GOAL");
        System.out.println(requestBody);

        return RequestBody.create(requestBody, MediaType.parse("application/json"));
    }

    @SneakyThrows
    private URL buildPutGoalUrl(String starlingGoalUrl, GoalUpdater goalUpdater) {
        URL finalURL = UriComponentsBuilder.fromHttpUrl(starlingGoalUrl)
                .buildAndExpand(
                        goalUpdater.getAccUId())
                .toUri()
                .toURL();
        return finalURL;
    }

    public void fetchGoals() {

    }

    @Override
    public void sendMoneyToGoal(GoalUpdater goalUpdater) {
        //First fetch goals ? if exist then update : create goal then send amount

        //Fetching the list of goals the user has
        Request request = new Request.Builder()
                .url(starlingGoalUrl)
                .header("Authorization",
                        "Bearer eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_31SSY7bMBD8ykDn4UCbtd3mlg_kAS12yyZMkQJJeTII8vdQImVZjpEbq6q3avbvRFibdAlMgiGN-sM6MFKocw_q-sH1mLwndu59RIrpMOR1z9IeclZiVrMGgVjNqyKnrCqKOvPB9GtKOo_KtqjzsnlPBLhANKe0WQjgXM_K_dASyfwU6Gv3TTa02OasaQtiJS-R9W1VMUz5QFTUeMLW13b6Sipk8KKF6uQzIMuQlUMJDNoqZTm0RVr1BccWfIa39ck5WRuy0uLU9qemZHUDNSup8lnNqWRZWUFe5lk_1PlimOuJlqWESRmX2hJ2hgDfNu6yjs8UjPRScN_TkyCQlBODIHPkpbDuwESAaPzgHaFwdxAU54BfRrpH7vjLCEdvMLuLNsL6b2RCobgJnEGG4B4kKB5H42CQca2c0TI0WpioaTUIM4ITWjE9sGFWaO-SvXffQGjNZ-v0uFmkEUQsLMkPos4dTJP8vqM1agSF4KhDkuRLbDBq5kpuMTIZGsiQn93-TwpjBG2SwMlvwNHZrD4eE_8VYyoZfoHN3UgO_DTQcQ9XNeLV1ATfRJsUQDQRwB7ExAjn6Clo-5M5A8oC3yf0NOtnee22n6Sd2rsFvDcMeCuw3IO_rVG4vabU3N_BQ4WVYHo5iGc2Zhk9CLmNH_wcqDXKECcxuQOwRyks18LNf5hlZ73PceCimwO31nlkwsL8l78qsYsvau1iKMovhLMkZHFhkSbnvMF5itDBeq1MG3zoeWS3Zkf2RT7TX-rOO1q_idvbMzXhEKm5t9z4FS7XsXV55NaoxxNaf-n5ppI_fwHdUwWL6wUAAA.aukHoE6d9eODqHJrsTJfkx0nzJm9j2k4fmwW8fYGTN6gB0FjScb31p-qPoH194vGWyi6IEdATJ8SF0X83x0Ew9VqItzEzYyGJ-fGRPslGxzfgvURAB83VT5OlahoZZCw1MrmrjaT5I1_gsEtXDlWrXaFbctgJid6yMTuh4jd98ZzJkB8RkdgB2LswIXB5pvMREmEKgkcycXPEPBdR0ot-BgSfeMbf6GaeqWHQaUrKZg-zdCVLyZIMsuG_NXo5LMhKa4Un2Q4fNmYDnsIYa7Mp6k71SWXKb2V4d54bnBDmDwhuc2pw3MwlySCYZERZP6Ipl6SAhOeBo8bYt17XJDg2ZTgA_myXGWTgalK8z214M0UH4pRwDOYloyovx5WGelo-0vAcZ_9Bi7TxmSPvFZVHKP36Uc_icCgrLSvcX4YDkEL3wrpZzH1_Oe6YxD6vf06OKdnwwWAGs03xy20gFL48e3O3UV3jVHDE4FgRmgh2fi6CBQ5b7XTh9IheJSK78xb1nDe6s5gWTEzGn5-oCh8_r-4TN-08jG5DdWhnAzGn24VMaYqGC33UrOeAo9xvmG8mdF1tSC37akyhNGhmq1AXR9PHnvexWHvkQnZEvUt2Mkar__lJ5jCRDt8zau8spj3xrRj77PzHmXQzxQoVMhPiJ-zJiPTQvxr0GFrxi3cNms")
                .header("Accept", "application/json")
                .build();

        try {
            String response = client.newCall(request)
                    .execute()
                    .body()
                    .string();
            boolean doesGoalExist = false;


            JSONObject jsonObject = new JSONObject(response);
            JSONArray savingsGoalList = jsonObject.getJSONArray("savingsGoalList");

            for (int i = 0; i < savingsGoalList.length(); i++) {
                if (savingsGoalList.getJSONObject(i).getString("name").equalsIgnoreCase(goalUpdater.getNameOfGoal())) {
                    doesGoalExist = true;
                    break;
                }
            }

            if (!doesGoalExist) {
                //create the goal
                create(goalUpdater);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
