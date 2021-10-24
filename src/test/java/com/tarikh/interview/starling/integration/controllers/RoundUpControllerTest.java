package com.tarikh.interview.starling.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikh.interview.starling.api.GoalTimeframeDTO;
import com.tarikh.interview.starling.api.ResponseDTO;
import com.tarikh.interview.starling.domain.PublishRoundUpPort;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;
import com.tarikh.interview.starling.integration.converters.TimestampDTOToTimestampDurationConverter;
import com.tarikh.interview.starling.integration.exceptions.NoPrimaryAccountsWereFoundException;
import com.tarikh.interview.starling.integration.exceptions.UnableToRetrieveTransactionException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoundUpController.class)
class RoundUpControllerTest {

    @MockBean
    private PublishRoundUpPort publishRoundUpPort;

    @MockBean
    private TimestampDTOToTimestampDurationConverter converter;

    @InjectMocks
    private RoundUpController roundUpController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    private final String goalName = "Fitness Goal";
    private final String timeStamp = "2021-10-18 21:46:07";
    private String accountHolderId = "BOB123";

    @Test
    public void contextLoads() throws Exception {
        assertThat(roundUpController).isNotNull();
    }

    @Test
    public void shouldReturnErrorCodeWhenFails() throws Exception {
        GoalTimeframeDTO goalTimeframeDTO = buildGoalTimeframeDTO(goalName, timeStamp);
        String responseBody = objectMapper.writeValueAsString(goalTimeframeDTO);

        when(converter.convert(any(), any())).thenCallRealMethod();

        when(publishRoundUpPort.publishToGoal(any(GoalTimeframe.class))).thenThrow(NoPrimaryAccountsWereFoundException.class);

        mockMvc.perform(post("/account/{accHolderUId}/saving-goals/transactions/roundup", accountHolderId)
                .content(responseBody)
                .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnErrorCodeWhenNoTransactionsPresent() throws Exception {
        GoalTimeframeDTO goalTimeframeDTO = buildGoalTimeframeDTO(goalName, timeStamp);
        String responseBody = objectMapper.writeValueAsString(goalTimeframeDTO);

        when(converter.convert(any(), any())).thenCallRealMethod();

        when(publishRoundUpPort.publishToGoal(any(GoalTimeframe.class))).thenThrow(UnableToRetrieveTransactionException.class);

        mockMvc.perform(post("/account/{accHolderUId}/saving-goals/transactions/roundup", accountHolderId)
                .content(responseBody)
                .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnResponseWhenSuccessful() throws Exception {
        GoalTimeframeDTO goalTimeframeDTO = buildGoalTimeframeDTO(goalName, timeStamp);
        String responseBody = objectMapper.writeValueAsString(goalTimeframeDTO);

        when(converter.convert(any(), any())).thenCallRealMethod();
        when(publishRoundUpPort.publishToGoal(any(GoalTimeframe.class))).thenReturn("random body");

        String contentAsString = mockMvc.perform(post("/account/{accHolderUId}/saving-goals/transactions/roundup", accountHolderId)
                .content(responseBody)
                .contentType("application/json"))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        ResponseDTO responseDTO = objectMapper.readValue(contentAsString, ResponseDTO.class);

        assertThat(responseDTO).as("The response")
                .hasFieldOrProperty("message");
    }

    private GoalTimeframeDTO buildGoalTimeframeDTO(String goalName, String timeStamp)
    {
        GoalTimeframeDTO dto = new GoalTimeframeDTO();
        dto.setSavingGoalName(goalName);
        dto.setTimestamp(timeStamp);
        return dto;
    }

}