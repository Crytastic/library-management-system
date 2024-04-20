package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.data.ReservationDAO;
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
    private final HashMap<Long, ReservationDAO> reservations = new HashMap<>();
    private static Long index = 1L;

    public List<ReservationDAO> findAll() {
        return reservations.values().stream().toList();
    }

    public ReservationDAO store(ReservationDAO reservationDAO) {
        reservationDAO.setId(index);
        reservations.put(reservationDAO.getId(), reservationDAO);
        index++;
        return reservationDAO;
    }

    public Optional<ReservationDAO> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public Optional<ReservationDAO> updateById(Long id, ReservationDAO reservationDAO) {
        reservations.put(id, reservationDAO);
        return Optional.ofNullable(reservationDAO);
    }

    public void deleteById(Long id) {
        reservations.remove(id);
    }

    public List<ReservationDAO> findAllActive() {
        OffsetDateTime currentDateTime = TimeProvider.now();
        return reservations
                .values()
                .stream()
                .filter(reservationDAO -> reservationDAO.getReservedTo().isAfter(currentDateTime) &&
                        reservationDAO.getReservedFrom().isBefore(currentDateTime)).toList();
    }

    public List<ReservationDAO> findAllExpired() {
        OffsetDateTime currentDateTime = TimeProvider.now();
        return reservations
                .values()
                .stream()
                .filter(reservationDAO -> reservationDAO.getReservedTo().isBefore(currentDateTime) &&
                        reservationDAO.getReservedFrom().isBefore(currentDateTime)).toList();
    }
}
