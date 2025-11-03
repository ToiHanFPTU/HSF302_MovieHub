package com.g_wuy.hsf302.moviehub.service;

import com.g_wuy.hsf302.moviehub.entity.Ticket;
import com.g_wuy.hsf302.moviehub.entity.User;

import java.util.List;

public interface TicketService {
    boolean bookTickets(Integer showtimeId, List<Integer> seatIds, User user);
    List<Ticket> getTicketsByTransaction(Integer transactionId);
    Ticket getTicketById(Integer ticketId);
    List<Ticket> getTicketsByShowtime(Integer showtimeId);
}
