package com.tarikh.interview.starling.integration.converters;

import com.tarikh.interview.starling.api.GoalTimeframeDTO;
import com.tarikh.interview.starling.domain.models.GoalTimeframe;
import com.tarikh.interview.starling.integration.exceptions.BadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TimestampDTOToTimestampDurationConverterTest {

    private TimestampDTOToTimestampDurationConverter converter;
    private final String accHolderId = "ACCHOLDER123";

    @BeforeEach
    public void setUp()
    {
        converter = new TimestampDTOToTimestampDurationConverter();

    }

    @Test
    public void shouldConvertSuccessfully()
    {
        String timestamp = "2021-10-18 21:46:07";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                                            .withZone(ZoneId.systemDefault());

        GoalTimeframe goals = converter.convert(accHolderId, buildGoalTimeframeDTO("2021-10-18 21:46:07", "GOALS"));

        assertThat(goals).as("The converted goal object")
                .hasFieldOrPropertyWithValue("goalName", "GOALS")
                .hasFieldOrPropertyWithValue("timestampBegin", Instant.from(formatter.parse(timestamp)))
                .hasFieldOrPropertyWithValue("accountHolderId", accHolderId);
    }


    @Test
    public void shouldThrowExceptionIfGoalNameIsNull()
    {
        String timestamp = "2021-10-18 21:46:07";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        assertThrows(BadException.class, () -> {
            //Code under test
            converter.convert(accHolderId, buildGoalTimeframeDTO("2021-10-18 21:46:07", ""));
        });
    }

    @Test
    public void shouldThrowExceptionIfTimestamptIsInvalid()
    {
        String timestamp = "2021-10-18";

        assertThrows(DateTimeParseException.class, () -> {
            //Code under test
            converter.convert(accHolderId, buildGoalTimeframeDTO(timestamp, "GOALS"));
        });
    }

    private GoalTimeframeDTO buildGoalTimeframeDTO(String timestamp, String goalName)
    {
        GoalTimeframeDTO dto = new GoalTimeframeDTO();
        dto.setTimestamp(timestamp);
        dto.setSavingGoalName(goalName);
        return dto;
    }

}