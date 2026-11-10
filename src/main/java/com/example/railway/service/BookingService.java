
package com.example.railway.service;

import com.example.railway.model.Booking;
import com.example.railway.model.Train;
import com.example.railway.repository.BookingRepository;
import com.example.railway.repository.TrainRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BookingService {

    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;

    public BookingService(
            TrainRepository trainRepository,
            BookingRepository bookingRepository
    ) {
        this.trainRepository = trainRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Booking bookTicket(
            Long trainId,
            String passengerName,
            String email,
            int age,
            LocalDate journeyDate,
            int numberOfSeats
    ) {

        if (passengerName == null || passengerName.isBlank()) {
            throw new IllegalArgumentException("Passenger name is required");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (age < 1 || age > 120) {
            throw new IllegalArgumentException("Invalid age");
        }

        if (journeyDate == null ||
                journeyDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid Journey Date");
        }

        // Validate requested seats
        if (numberOfSeats < 1 || numberOfSeats > 6) {
            throw new IllegalArgumentException(
                    "You can book between 1 and 6 seats at a time"
            );
        }

        // Lock train while checking availability
        Train train = trainRepository.findByIdForUpdate(trainId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Train is not found"));

        if (!train.isActive()) {
            throw new IllegalArgumentException("Train is not Active");
        }

        // Calculate already booked seats
        long bookedSeats =
                bookingRepository.countByTrain_IdAndJourneyDateAndStatus(
                        trainId,
                        journeyDate,
                        "CONFIRMED"
                );

        int availableSeats =
                train.getTotalSeats() - (int) bookedSeats;

        // Check requested seats against availability
        if (availableSeats < numberOfSeats) {
            throw new IllegalArgumentException(
                    "Only " + availableSeats + " seats are available"
            );
        }

        // Create booking
        Booking booking = new Booking();

        booking.setPnr(
                "PNR" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 10)
                        .toUpperCase()
        );

        booking.setPassengerName(passengerName);
        booking.setEmail(email);
        booking.setAge(age);
        booking.setJourneyDate(journeyDate);
        booking.setStatus("CONFIRMED");
        booking.setNumberOfSeats(numberOfSeats);
        booking.setTrain(train);

        return bookingRepository.save(booking);
    }

    public Booking getBookingByPnr(String pnr) {

        Booking booking = bookingRepository.findByPnr(pnr);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        return booking;
    }

    @Transactional
    public Booking cancelTicket(String pnr) {

        Booking booking = bookingRepository.findByPnr(pnr);

        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new IllegalArgumentException(
                    "Ticket already cancelled"
            );
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }
}
