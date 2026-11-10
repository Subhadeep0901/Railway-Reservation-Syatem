
package com.example.railway.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.example.railway.model.Booking;
import com.example.railway.model.Train;
import com.example.railway.repository.TrainRepository;
import com.example.railway.repository.BookingRepository;
import com.example.railway.service.BookingService;
import com.example.railway.service.EmailService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    public BookingController(
            BookingService bookingService,
            TrainRepository trainRepository,
            BookingRepository bookingRepository,
            EmailService emailService) {

        this.bookingService = bookingService;
        this.trainRepository = trainRepository;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }

    // Calculate remaining seats
    private int getRemainingSeats(Long trainId, LocalDate journeyDate) {

        Train train = trainRepository.findById(trainId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Train not found"));

        long bookedSeats = bookingRepository
                .countByTrain_IdAndJourneyDateAndStatus(
                        trainId,
                        journeyDate,
                        "CONFIRMED"
                );

        return (int) Math.max(
                0L,
                train.getTotalSeats() - bookedSeats
        );
    }

    // Display booking form
    @GetMapping("/booking")
    public String showBookingForm(
            @RequestParam Long trainId,
            @RequestParam LocalDate journeyDate,
            Model model) {

        Train train = trainRepository.findById(trainId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Train not found"));

        int remainingSeats = getRemainingSeats(trainId, journeyDate);

        model.addAttribute("train", train);
        model.addAttribute("journeyDate", journeyDate);
        model.addAttribute("remainingSeats", remainingSeats);

        return "booking";
    }

    // Book ticket
    @PostMapping("/book")
    public String bookTicket(
            @RequestParam Long trainId,
            @RequestParam String passengerName,
            @RequestParam String email,
            @RequestParam int age,
            @RequestParam LocalDate journeyDate,
            @RequestParam int numberOfSeats,
            Model model) {

        try {

            Booking booking = bookingService.bookTicket(
                    trainId,
                    passengerName,
                    email,
                    age,
                    journeyDate,
                    numberOfSeats
            );

            // Send confirmation email
            try {
                emailService.sendBookingConfirmation(booking);

            } catch (Exception e) {
                System.out.println(
                        "Email sending failed: " + e.getMessage()
                );
            }

            model.addAttribute("booking", booking);

            return "success";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());

            Train train = trainRepository.findById(trainId)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Train not found"));

            int remainingSeats = getRemainingSeats(
                    trainId,
                    journeyDate
            );

            model.addAttribute("train", train);
            model.addAttribute("journeyDate", journeyDate);
            model.addAttribute("remainingSeats", remainingSeats);

            return "booking";
        }
    }

    // Cancel ticket
    @PostMapping("/cancel-ticket")
    public String cancelTicket(
            @RequestParam String pnr,
            RedirectAttributes redirectAttributes) {

        try {

            bookingService.cancelTicket(pnr);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Ticket cancelled successfully"
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/";
    }

    // Search booking using PNR
    @GetMapping("/search-booking")
    public String searchBooking(
            @RequestParam String pnr,
            Model model) {

        try {

            Booking booking = bookingService.getBookingByPnr(pnr);

            model.addAttribute("booking", booking);

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());
        }

        return "success";
    }

    // Download ticket PDF
    @GetMapping("/download-ticket")
    public void downloadTicket(
            @RequestParam String pnr,
            HttpServletResponse response)
            throws IOException, DocumentException {

        Booking booking = bookingService.getBookingByPnr(pnr.trim());

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=ticket-" + pnr + ".pdf"
        );

        Document document = new Document();

        PdfWriter.getInstance(
                document,
                response.getOutputStream()
        );

        document.open();

        Font titleFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                20
        );

        Paragraph title = new Paragraph(
                "RAILWAY E-TICKET",
                titleFont
        );

        title.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(title);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Booking Information"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(100);

        table.addCell("PNR Number");
        table.addCell(booking.getPnr());

        table.addCell("Passenger Name");
        table.addCell(booking.getPassengerName());

        table.addCell("Email");
        table.addCell(booking.getEmail());

        table.addCell("Age");
        table.addCell(String.valueOf(booking.getAge()));

        table.addCell("Number of Seats");
        table.addCell(String.valueOf(booking.getNumberOfSeats()));

        table.addCell("Train Number");
        table.addCell(booking.getTrain().getTrainNumber());

        table.addCell("Train Name");
        table.addCell(booking.getTrain().getTrainName());

        table.addCell("Source");
        table.addCell(booking.getTrain().getSource());

        table.addCell("Destination");
        table.addCell(booking.getTrain().getDestination());

        table.addCell("Journey Date");
        table.addCell(booking.getJourneyDate().toString());

        table.addCell("Departure Time");
        table.addCell(booking.getTrain().getDepartureTime());

        table.addCell("Arrival Time");
        table.addCell(booking.getTrain().getArrivalTime());

        table.addCell("Booking Status");
        table.addCell(booking.getStatus());

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(
                new Paragraph("Thank you for booking with us!")
        );

        document.close();
    }
}
