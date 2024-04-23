package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Modifying
    @Query("UPDATE Reservation r SET " +
            "r.bookId = coalesce(:bookId, r.bookId), " +
            "r.reserveeId = coalesce(:reserveeId, r.reserveeId)," +
            "r.reservedFrom = coalesce(:reservedFrom, r.reservedFrom)," +
            "r.reservedTo = coalesce(:reservedTo, r.reservedTo) WHERE r.id = :id")
    int updateById(Long id, Long bookId, Long reserveeId, OffsetDateTime reservedFrom, OffsetDateTime reservedTo);


    @Query("SELECT r FROM Reservation r WHERE r.reservedFrom < :currentDate AND r.reservedTo > :currentDate")
    List<Reservation> findAllActive(OffsetDateTime currentDate);

    @Query("SELECT r FROM Reservation r WHERE r.reservedFrom < :currentDate AND r.reservedTo < :currentDate")
    List<Reservation> findAllExpired(OffsetDateTime currentDate);
}
