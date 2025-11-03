package com.g_wuy.hsf302.moviehub.service.impl;

import com.g_wuy.hsf302.moviehub.entity.*;
import com.g_wuy.hsf302.moviehub.repository.SeatRepository;
import com.g_wuy.hsf302.moviehub.repository.ShowtimeRepository;
import com.g_wuy.hsf302.moviehub.repository.TicketRepository;
import com.g_wuy.hsf302.moviehub.repository.TransactionRepository;
import com.g_wuy.hsf302.moviehub.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final BigDecimal PRICE_PER_TICKET = BigDecimal.valueOf(100000);

    @Override
    @Transactional
    public boolean bookTickets(Integer showtimeId, List<Integer> seatIds, User user) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        // Kiểm tra ghế đã đặt
        for (Integer seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            if (ticketRepository.existsByShowtimeIDAndSeatID(showtime, seat)) {
                return false; // ghế trùng
            }
        }

        // Tạo transaction
        Transaction transaction = new Transaction();
        transaction.setUserID(user);
        transaction.setPaymentMethod("Cash");
        transaction.setTransactionDate(Instant.now());
        transaction.setTotalAmount(BigDecimal.ZERO);
        transactionRepository.save(transaction);

        // Tạo vé và tính tổng
        BigDecimal total = BigDecimal.ZERO;
        for (Integer seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId).get();
            Ticket ticket = new Ticket();
            ticket.setShowtimeID(showtime);
            ticket.setSeatID(seat);
            ticket.setUserID(user);
            ticket.setPrice(PRICE_PER_TICKET);
            ticket.setBookingTime(Instant.now());
            ticket.setTransactionID(transaction);
            ticketRepository.save(ticket);
            total = total.add(PRICE_PER_TICKET);
        }

        transaction.setTotalAmount(total);
        transactionRepository.save(transaction);

        return true;
    }

    @Override
    public List<Ticket> getTicketsByTransaction(Integer transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return ticketRepository.findByTransactionIDWithShowtimeAndMovie(transaction);
    }

    @Override
    public Ticket getTicketById(Integer ticketId) {
        return ticketRepository.findByIdWithShowtimeAndMovie(ticketId);
    }

    @Override
    public List<Ticket> getTicketsByShowtime(Integer showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
        return ticketRepository.findByShowtimeID(showtime);
    }
}

