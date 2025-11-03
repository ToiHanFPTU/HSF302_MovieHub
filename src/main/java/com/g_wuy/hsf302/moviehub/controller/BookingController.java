// BookingController.java
package com.g_wuy.hsf302.moviehub.controller;

import com.g_wuy.hsf302.moviehub.entity.Seat;
import com.g_wuy.hsf302.moviehub.entity.Ticket;
import com.g_wuy.hsf302.moviehub.entity.User;
import com.g_wuy.hsf302.moviehub.repository.SeatRepository;
import com.g_wuy.hsf302.moviehub.service.TicketService;
import com.g_wuy.hsf302.moviehub.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private SeatRepository seatRepository;

    // Trang chọn ghế
    @GetMapping("/ticket-book")
    public String showBookingPage(@RequestParam Integer showtimeId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }

        List<Seat> seats = seatRepository.findByRoomID(
                showtimeRepository.findById(showtimeId).get().getRoomID()
        );

        List<Ticket> bookedTickets = ticketService.getTicketsByShowtime(showtimeId);
        seats.forEach(seat -> seat.setBooked(
                bookedTickets.stream()
                        .anyMatch(ticket -> ticket.getSeatID().getId().equals(seat.getId()))
        ));

        model.addAttribute("showtimeId", showtimeId);
        model.addAttribute("seats", seats);
        return "booking/ticket_book";
    }

    // Đặt vé
    @PostMapping("/book-ticket")
    public String bookTickets(@RequestParam("showtimeId") Integer showtimeId,
                              @RequestParam("seatIds") String seatIds,
                              HttpServletRequest request,
                              Model model) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }

        User user = (User) session.getAttribute("user");

        List<Integer> seatList = Arrays.stream(seatIds.split(","))
                .map(Integer::parseInt)
                .toList();

        boolean success = ticketService.bookTickets(showtimeId, seatList, user);

        if (success) {
            model.addAttribute("message", "Đặt vé thành công!");
        } else {
            model.addAttribute("message", "Có ghế đã được đặt, vui lòng chọn lại!");
        }

        // Trả lại trang đặt vé với danh sách ghế mới
        return "redirect:/ticket-book?showtimeId=" + showtimeId;
    }

    // Xem danh sách vé theo transaction
    @GetMapping("/ticket-detail/{transactionId}")
    public String viewTicketsByTransaction(@PathVariable Integer transactionId,
                                           HttpServletRequest request,
                                           Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }

        List<Ticket> tickets = ticketService.getTicketsByTransaction(transactionId);
        model.addAttribute("tickets", tickets);
        return "booking/list_tickets";
    }

    // Xem chi tiết từng vé
    @GetMapping("/ticket/{ticketId}")
    public String viewSingleTicket(@PathVariable Integer ticketId,
                                   HttpServletRequest request,
                                   Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/auth/login";
        }

        Ticket ticket = ticketService.getTicketById(ticketId);
        model.addAttribute("ticket", ticket);
        return "booking/ticket_detail";
    }
}
