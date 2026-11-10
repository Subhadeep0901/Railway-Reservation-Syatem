
package com.example.railway.repository;

import com.example.railway.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findByPnr(String pnr);

    // Calculate total seats booked for a train on a particular date
    @Query("""
        SELECT COALESCE(SUM(b.numberOfSeats), 0)
        FROM Booking b
        WHERE b.train.id = :trainId
        AND b.journeyDate = :journeyDate
        AND b.status = :status
    """)
    long countByTrain_IdAndJourneyDateAndStatus(
            @Param("trainId") Long trainId,
            @Param("journeyDate") LocalDate journeyDate,
            @Param("status") String status
    );

    long countByStatus(String status);
}
