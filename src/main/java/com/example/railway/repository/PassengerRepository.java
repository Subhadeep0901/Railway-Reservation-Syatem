package com.example.railway.repository;

import com.example.railway.model.Passenger;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Optional<Passenger> findByEmail(String email);

    boolean existsByEmail(String email);
}