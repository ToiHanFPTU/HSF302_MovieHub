package com.g_wuy.hsf302.moviehub.model.request;

import lombok.Data;

import java.util.List;

@Data
public class BookTicketsRequest {
    private Integer showtimeId;
    private List<Integer> seatIds;
}