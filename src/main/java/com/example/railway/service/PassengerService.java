package com.example.railway.service;

import com.example.railway.model.Passenger;
import com.example.railway.repository.PassengerRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final PasswordEncoder passwordEncoder;

    public PassengerService(
            PassengerRepository passengerRepository,
            PasswordEncoder passwordEncoder) {

        this.passengerRepository = passengerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Passenger registerPassenger(
            String name,
            String email,
            String phone,
            String password) {

        if (passengerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered!"
            );
        }

        Passenger passenger = new Passenger();

        passenger.setName(name);
        passenger.setEmail(email);
        passenger.setPhone(phone);
        passenger.setPassword(passwordEncoder.encode(password));

        return passengerRepository.save(passenger);
    }
}