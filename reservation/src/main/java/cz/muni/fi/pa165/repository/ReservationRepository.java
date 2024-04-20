package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.util.TimeProvider;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    public List<Reservation> findAll() {
        return reservations.values().stream().toList();
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public Optional<Reservation> updateById(Long id, Reservation reservation) {
        reservations.put(id, reservation);
        return Optional.ofNullable(reservation);
    }

    public void deleteById(Long id) {
        reservations.remove(id);
    }

    public List<Reservation> findAllActive() {
        OffsetDateTime currentDateTime = TimeProvider.now();
        return reservations
                .values()
                .stream()
                .filter(reservationDAO -> reservationDAO.getReservedTo().isAfter(currentDateTime) &&
                        reservationDAO.getReservedFrom().isBefore(currentDateTime)).toList();
    }

    public List<Reservation> findAllExpired() {
        OffsetDateTime currentDateTime = TimeProvider.now();
        return reservations
                .values()
                .stream()
                .filter(reservationDAO -> reservationDAO.getReservedTo().isBefore(currentDateTime) &&
                        reservationDAO.getReservedFrom().isBefore(currentDateTime)).toList();
    }
}
