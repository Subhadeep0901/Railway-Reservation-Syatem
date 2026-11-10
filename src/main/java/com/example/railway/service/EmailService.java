package com.example.railway.service;

import com.example.railway.model.Booking;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingConfirmation(Booking booking) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(booking.getEmail());

        message.setSubject("Railway Ticket Booking Confirmation");

        message.setText(
            "Dear " + booking.getPassengerName() + ",\n\n" +
            "Your railway ticket has been booked successfully!\n\n" +
            "PNR: " + booking.getPnr() + "\n" +
            "Train: " + booking.getTrain().getTrainName() + "\n" +
            "Train Number: " + booking.getTrain().getTrainNumber() + "\n" +
            "From: " + booking.getTrain().getSource() + "\n" +
            "To: " + booking.getTrain().getDestination() + "\n" +
            "Journey Date: " + booking.getJourneyDate() + "\n" +
            "Status: " + booking.getStatus() + "\n\n" +
            "Thank you for choosing our Railway Reservation System."
        );

        mailSender.send(message);
    }
}