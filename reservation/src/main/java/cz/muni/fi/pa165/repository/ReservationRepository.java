package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.util.TimeProvider;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * Repository layer for managing book reservations.
 * Provides methods for storing, retrieving, updating, and deleting reservations.
 *
 * @author Martin Suchánek
 */
@Repository
public class ReservationRepository {
    private final HashMap<Long, Reservation> reservations = new HashMap<>();
    private static Long index = 1L;

    public List<Reservation> findAllExpired() {
        OffsetDateTime currentDateTime = TimeProvider.now();
        return reservations
                .values()
                .stream()
                .filter(reservationDAO -> reservationDAO.getReservedTo().isBefore(currentDateTime) &&
                        reservationDAO.getReservedFrom().isBefore(currentDateTime)).toList();
    }
}
