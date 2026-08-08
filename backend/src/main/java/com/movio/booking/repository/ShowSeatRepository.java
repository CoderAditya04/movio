package com.movio.booking.repository;

import com.movio.booking.entity.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShowId(Long showId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShowSeat s WHERE s.id = :id")
    Optional<ShowSeat> findByIdForUpdate(@Param("id") Long id);

    // for the cleanup job later
    List<ShowSeat> findByStatusAndLockedAtBefore(ShowSeat.Status status, java.time.LocalDateTime cutoff);
}