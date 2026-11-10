
package com.example.railway.controller;

import com.example.railway.model.Passenger;
import com.example.railway.repository.PassengerRepository;
import com.example.railway.service.PassengerService;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PassengerController {

    private final PassengerService passengerService;
    private final PassengerRepository passengerRepository;
    private final PasswordEncoder passwordEncoder;

    public PassengerController(
            PassengerService passengerService,
            PassengerRepository passengerRepository,
            PasswordEncoder passwordEncoder) {

        this.passengerService = passengerService;
        this.passengerRepository = passengerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerPassenger(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            Model model) {

        try {
            passengerService.registerPassenger(
                    name, email, phone, password);

            model.addAttribute(
                    "message",
                    "Registration successful! Please login.");

            return "passenger-login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/passenger/login")
    public String showLoginPage() {
        return "passenger-login";
    }

    @PostMapping("/passenger/login")
    public String passengerLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Passenger passenger =
                passengerRepository.findByEmail(email).orElse(null);

        if (passenger == null ||
                !passwordEncoder.matches(
                        password, passenger.getPassword())) {

            model.addAttribute(
                    "error", "Invalid email or password!");

            return "passenger-login";
        }

        session.setAttribute("passengerId", passenger.getId());
        session.setAttribute("passengerName", passenger.getName());

        return "redirect:/";
    }

    @PostMapping("/passenger/logout")
    public String passengerLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
