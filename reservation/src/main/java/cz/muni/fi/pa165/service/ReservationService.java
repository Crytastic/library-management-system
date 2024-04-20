package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.repository.ReservationRepository;
import cz.muni.fi.pa165.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing book reservations.
 * Provides methods to interact with reservations.
 *
 * @author Martin Suchánek
 */
@Service
public class ReservationService {

    ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation createReservation(String book, String reservedBy) {
        Reservation reservation = new Reservation(book, reservedBy, TimeProvider.now(), getDefaultReservationCancelDate());
        return reservationRepository.store(reservation);
    }

    private OffsetDateTime getDefaultReservationCancelDate() {
        return TimeProvider.now().plusDays(3);
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public Optional<Reservation> updateById(Long id, String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (optionalReservation.isEmpty()) {
            return Optional.empty();
        }

        Reservation reservation = optionalReservation.get();

        if (book != null) reservation.setBook(book);
        if (reservedBy != null) reservation.setReservedBy(reservedBy);
        if (reservedFrom != null) reservation.setReservedFrom(reservedFrom);
        if (reservedTo != null) reservation.setReservedTo(reservedTo);

        return reservationRepository.updateById(id, reservation);

    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findAllActive() {
        return reservationRepository.findAllActive();
    }

    public List<Reservation> findAllExpired() {
        return reservationRepository.findAllExpired();
    }
}
