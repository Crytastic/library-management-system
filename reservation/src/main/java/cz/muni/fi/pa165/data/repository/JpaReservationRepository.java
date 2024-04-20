package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;

public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {

    @Modifying
    @Query("UPDATE Reservation r SET " +
            "r.book = coalesce(:book, r.book), " +
            "r.reservedBy = coalesce(:reservedBy, r.reservedBy)," +
            "r.reservedFrom = coalesce(:reservedFrom, r.reservedFrom)," +
            "r.reservedTo = coalesce(:reservedTo, r.reservedTo) WHERE r.id = :id")
    int updateById(Long id, String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo);

}
