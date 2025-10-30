package com.g_wuy.hsf302.moviehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "RoomName", nullable = false, length = 50)
    private String roomName;

    @NotNull
    @Column(name = "Capacity", nullable = false)
    private Integer capacity;

}