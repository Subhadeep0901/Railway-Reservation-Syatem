
package com.example.railway.controller;

import com.example.railway.model.Train;
import com.example.railway.repository.TrainRepository;
import com.example.railway.repository.BookingRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.*;

@Controller
public class HomeController {

    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;

    public HomeController(TrainRepository trainRepository,
                          BookingRepository bookingRepository) {
        this.trainRepository = trainRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/")
    public String home(HttpSession session) {

        if (session.getAttribute("passengerId") == null) {
            return "redirect:/passenger/login";
        }

        return "index";
    }

    @GetMapping("/search-trains")
    public String searchTrains(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam LocalDate journeyDate,
            Model model) {

        if (journeyDate.isBefore(LocalDate.now())) {
            model.addAttribute("error",
                    "Journey date cannot be in the past.");
            return "index";
        }

        List<Train> trains = trainRepository
                .findBySourceIgnoreCaseAndDestinationIgnoreCaseAndActiveTrue(
                        source, destination);

        List<Map<String, Object>> trainResults = new ArrayList<>();

        for (Train train : trains) {

            // Calculates total seats already booked
            long bookedSeats = bookingRepository
                    .countByTrain_IdAndJourneyDateAndStatus(
                            train.getId(),
                            journeyDate,
                            "CONFIRMED");

            // Calculates remaining seats
            int remainingSeats = Math.max(
                    0,
                    train.getTotalSeats() - (int) bookedSeats
            );

            Map<String, Object> result = new HashMap<>();

            result.put("train", train);
            result.put("remainingSeats", remainingSeats);

            trainResults.add(result);
        }

        model.addAttribute("trainResults", trainResults);
        model.addAttribute("journeyDate", journeyDate);

        return "trains";
    }
}
