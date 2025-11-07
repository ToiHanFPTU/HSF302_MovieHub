package com.g_wuy.hsf302.moviehub.model.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ShowtimeDTO {
    private Integer showtimeId;
    private String roomName;
    private Instant startTime;
    private Instant endTime;
}
