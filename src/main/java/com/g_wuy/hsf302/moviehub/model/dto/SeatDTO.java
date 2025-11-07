package com.g_wuy.hsf302.moviehub.model.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private Integer seatId;
    private String seatNumber;
    private String seatType;
    private boolean booked;
}
