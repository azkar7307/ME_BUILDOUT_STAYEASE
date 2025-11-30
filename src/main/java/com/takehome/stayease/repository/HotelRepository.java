package com.takehome.stayease.repository;

import java.util.List;
import com.takehome.stayease.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("SELECT h FROM Hotel h where h.availableRooms > 0")
    List<Hotel> findAllAvailable();
}
