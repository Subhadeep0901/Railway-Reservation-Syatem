package com.example.railway.controller;

import com.example.railway.model.Train;
import com.example.railway.repository.TrainRepository;
import com.example.railway.repository.BookingRepository;
import com.example.railway.service.BookingService;
import com.example.railway.model.Booking;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController{
    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    public AdminController(TrainRepository trainRepository,BookingRepository bookingRepository,BookingService bookingService
    ){
        this.trainRepository=trainRepository;
        this.bookingRepository=bookingRepository;
        this.bookingService=bookingService;
    }
    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model){
        model.addAttribute("totalTrains",trainRepository.count());
        model.addAttribute("totalBookings",bookingRepository.count());
        model.addAttribute("confirmedBookings",bookingRepository.countByStatus("CONFIRMED"));
        model.addAttribute("cancelledBookings",bookingRepository.countByStatus("CANCELLED"));
        model.addAttribute("bookings",bookingRepository.findAll());
        return "admin-dashboard";
    }
    @PostMapping("/admin/add-train")
    public String addTrain(
        @RequestParam String trainNumber,
        @RequestParam String trainName,
        @RequestParam String source,
        @RequestParam String destination,
        @RequestParam String departureTime,
        @RequestParam String arrivalTime,
        @RequestParam int totalSeats,
        Model model,
        RedirectAttributes redirectAttributes
        ){
            if(trainRepository.findByTrainNumber(trainNumber).isPresent()){
                model.addAttribute("error","Train number already exists");
                return "admin";
            }
            if(totalSeats<=0){
                model.addAttribute("error","Total seats must be greater than zero");
                return "admin";
            }
            Train train=new Train();
            train.setTrainNumber(trainNumber);
            train.setTrainName(trainName);
            train.setSource(source);
            train.setDestination(destination);
            train.setDepartureTime(departureTime);
            train.setArrivalTime(arrivalTime);
            train.setTotalSeats(totalSeats);
            train.setActive(true);

            trainRepository.save(train);
            redirectAttributes.addFlashAttribute(
                "message","Train added successFully!"
            );
            return "redirect:/admin";
        }
        @GetMapping("/admin/trains")
        public String manageTrains(Model model){
            model.addAttribute("trains",trainRepository.findAll());
            return "manage-trains";
        }
        @GetMapping("/admin/edit-train/{id}")
        public String editTrain(
            @PathVariable Long id,
            Model model
        ){
            Train train=trainRepository.findById(id).
            orElseThrow(()->
            new IllegalArgumentException("Train not found"));
            model.addAttribute("train",train);
            return "edit-train";
        }
        @PostMapping("/admin/update-train")
public String updateTrain(
        @RequestParam Long id,
        @RequestParam String trainNumber,
        @RequestParam String trainName,
        @RequestParam String source,
        @RequestParam String destination,
        @RequestParam String departureTime,
        @RequestParam String arrivalTime,
        @RequestParam int totalSeats,
        RedirectAttributes redirectAttributes) {

    Train train = trainRepository.findById(id)
            .orElseThrow(() ->
                    new IllegalArgumentException("Train not found"));

    if (totalSeats <= 0) {
        redirectAttributes.addFlashAttribute(
                "error", "Total seats must be greater than zero");
        return "redirect:/admin/trains";
    }

    train.setTrainNumber(trainNumber);
    train.setTrainName(trainName);
    train.setSource(source);
    train.setDestination(destination);
    train.setDepartureTime(departureTime);
    train.setArrivalTime(arrivalTime);
    train.setTotalSeats(totalSeats);

    trainRepository.save(train);

    redirectAttributes.addFlashAttribute(
            "message", "Train updated successfully!");

    return "redirect:/admin/trains";
}


@PostMapping("/admin/deactivate-train/{id}")
public String deactivateTrain(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {

    Train train = trainRepository.findById(id)
            .orElseThrow(() ->
                    new IllegalArgumentException("Train not found"));

    train.setActive(false);
    trainRepository.save(train);

    redirectAttributes.addFlashAttribute(
            "message", "Train deactivated successfully!");

    return "redirect:/admin/trains";
}
@PostMapping("/admin/activate-train/{id}")
public String activateTrain(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {

    Train train = trainRepository.findById(id)
            .orElseThrow(() ->
                    new IllegalArgumentException("Train not found"));

    train.setActive(true);
    trainRepository.save(train);

    redirectAttributes.addFlashAttribute(
            "message", "Train activated successfully!");

    return "redirect:/admin/trains";
}
@PostMapping("/admin/cancel-booking/{pnr}")
public String cancelBooking(
        @PathVariable String pnr,
        RedirectAttributes redirectAttributes) {

    try {
        bookingService.cancelTicket(pnr);

        redirectAttributes.addFlashAttribute(
                "message", "Booking cancelled successfully!");

    } catch (IllegalArgumentException e) {

        redirectAttributes.addFlashAttribute(
                "error", e.getMessage());
    }

    return "redirect:/admin/dashboard";
}
@GetMapping("/admin/booking-details/{pnr}")
public String bookingDetails(@PathVariable String pnr, Model model) {

    Booking booking = bookingService.getBookingByPnr(pnr);

    model.addAttribute("booking", booking);

    return "booking-details";
}
}