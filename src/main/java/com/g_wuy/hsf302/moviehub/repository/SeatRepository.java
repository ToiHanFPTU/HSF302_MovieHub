package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.Room;
import com.g_wuy.hsf302.moviehub.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> findByRoomID(Room room);
}
