package com.g_wuy.hsf302.moviehub.model.response;

import com.g_wuy.hsf302.moviehub.model.dto.TicketDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookTicketsResponse {
    private Integer transactionId;
    private BigDecimal totalAmount;
    private List<TicketDTO> tickets;
}