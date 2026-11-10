package com.example.railway.repository;
import com.example.railway.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long>{
    List<Train> findBySourceIgnoreCaseAndDestinationIgnoreCaseAndActiveTrue(String source,String destination);
    List<Train> findByActiveTrue();
    Optional<Train> findByTrainNumber(String trainNumber);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Train t where t.id=:id")
    Optional<Train> findByIdForUpdate(@Param("id") Long id);
}