package com.g_wuy.hsf302.moviehub.service.impl;


import com.g_wuy.hsf302.moviehub.entity.*;
import com.g_wuy.hsf302.moviehub.model.dto.SeatDTO;
import com.g_wuy.hsf302.moviehub.model.dto.ShowtimeDTO;
import com.g_wuy.hsf302.moviehub.model.dto.TicketDTO;
import com.g_wuy.hsf302.moviehub.model.request.BookTicketsRequest;
import com.g_wuy.hsf302.moviehub.model.response.BookTicketsResponse;
import com.g_wuy.hsf302.moviehub.model.response.MovieDetailResponse;
import com.g_wuy.hsf302.moviehub.repository.*;
import com.g_wuy.hsf302.moviehub.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    // Xem chi tiết phim + các khung giờ
    @Override
    public MovieDetailResponse getMovieDetail(Integer movieId, Movie movie) {
        List<Showtime> showtimes = showtimeRepository.findByMovieIdOrderByStartTime(movieId);

        MovieDetailResponse response = new MovieDetailResponse();
        response.setMovieId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setDuration(movie.getDuration());
        response.setLanguage(movie.getLanguage());
        response.setReleaseDate(movie.getReleaseDate());
        response.setDescription(movie.getDescription());
        response.setImageUrl(movie.getImageUrl());

        // --- Map categories ---
        List<String> categoryNames = movie.getCategories().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
        response.setCategories(categoryNames);

        // --- Map showtimes ---
        List<ShowtimeDTO> showtimeDTOs = showtimes.stream().map(st -> {
            ShowtimeDTO dto = new ShowtimeDTO();
            dto.setShowtimeId(st.getId());
            dto.setRoomName(st.getRoom().getRoomName());
            dto.setStartTime(st.getStartTime());
            dto.setEndTime(st.getEndTime());
            return dto;
        }).collect(Collectors.toList());
        response.setShowtimes(showtimeDTOs);

        return response;
    }



    @Override
    public List<SeatDTO> getAvailableSeats(Integer showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        List<Seat> allSeats = seatRepository.findByRoomId(showtime.getRoom().getId());
        List<Ticket> bookedTickets = ticketRepository.findByShowtimeId(showtimeId);

        Set<Integer> bookedSeatIds = bookedTickets.stream()
                .map(t -> t.getSeat().getId())
                .collect(Collectors.toSet());

        return allSeats.stream()
                .filter(s -> !bookedSeatIds.contains(s.getId()))
                .map(s -> {
                    SeatDTO dto = new SeatDTO();
                    dto.setSeatId(s.getId());
                    dto.setSeatNumber(s.getSeatNumber());
                    dto.setSeatType(s.getSeatType());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public BookTicketsResponse bookTickets(User sessionUser, BookTicketsRequest request, BigDecimal pricePerSeat) {

        // Lấy user từ session
        User user = userRepository.findUserByEmail(sessionUser.getEmail());
        if (user == null) throw new RuntimeException("User not found");

        // Lấy showtime
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());
        if (seats.isEmpty()) throw new RuntimeException("No seats selected");

        // Kiểm tra ghế đã đặt
        for (Seat seat : seats) {
            if (ticketRepository.existsByShowtimeIdAndSeatId(showtime.getId(), seat.getId())) {
                throw new RuntimeException("Ghế đã được đặt: " + seat.getSeatNumber());
            }
        }

        // Tạo vé trước (chưa set transaction)
        List<Ticket> tickets = new ArrayList<>();
        Instant now = Instant.now();
        for (Seat seat : seats) {
            Ticket ticket = new Ticket();
            ticket.setShowtime(showtime);
            ticket.setSeat(seat);
            ticket.setUser(user);
            ticket.setPrice(pricePerSeat);
            ticket.setBookingTime(now);
            ticketRepository.save(ticket);
            tickets.add(ticket);
        }

        // Tạo transaction và link vào vé
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(Instant.now());
        transaction.setStatus("PENDING");
        transaction.setPaymentMethod("BANKING");
        transaction.setTotalAmount(pricePerSeat.multiply(BigDecimal.valueOf(seats.size())));
        transaction.setUser(user);
        transactionRepository.save(transaction);

        // Cập nhật transaction cho các vé
        for (Ticket ticket : tickets) {
            ticket.setTransaction(transaction);
            ticketRepository.save(ticket);
        }

        // Tạo response
        BookTicketsResponse resp = new BookTicketsResponse();
        resp.setTransactionId(transaction.getId());
        resp.setTotalAmount(transaction.getTotalAmount());

        List<TicketDTO> ticketDTOs = tickets.stream().map(t -> {
            TicketDTO dto = new TicketDTO();
            dto.setMovieTitle(t.getShowtime().getMovie().getTitle());
            dto.setSeatNumber(t.getSeat().getSeatNumber());
            dto.setPrice(t.getPrice());
            return dto;
        }).collect(Collectors.toList());

        resp.setTickets(ticketDTOs);
        return resp;
    }
    @Override
    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByUserOrderByBookingTimeDesc(user);
    }
}
