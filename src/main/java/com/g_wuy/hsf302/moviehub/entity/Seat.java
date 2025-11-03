package com.g_wuy.hsf302.moviehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SeatID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RoomID", nullable = false)
    private Room roomID;

    @Size(max = 10)
    @NotNull
    @Column(name = "SeatNumber", nullable = false, length = 10)
    private String seatNumber;

    @Size(max = 50)
    @Column(name = "SeatType", length = 50)
    private String seatType;

    @Transient
    private boolean booked;

}