package com.g_wuy.hsf302.moviehub.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketDTO {
    private String movieTitle;
    private String seatNumber;
    private BigDecimal price;
}