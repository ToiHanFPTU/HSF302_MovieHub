package com.g_wuy.hsf302.moviehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Size(max = 10)
    @NotNull
    @Nationalized
    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Size(max = 50)
    @Nationalized
    @Column(name = "seat_type", length = 50)
    private String seatType;

}