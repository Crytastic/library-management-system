package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.data.repository.ReservationRepository;
import cz.muni.fi.pa165.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation createReservation(String book, String reservedBy) {
        return reservationRepository.save(new Reservation(book, reservedBy, TimeProvider.now(), getDefaultReservationCancelDate()));
    }

    private OffsetDateTime getDefaultReservationCancelDate() {
        return TimeProvider.now().plusDays(3);
    }

    @Transactional
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Transactional
    public int updateById(Long id, String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        return reservationRepository.updateById(id, book, reservedBy, reservedFrom, reservedTo);

    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public List<Reservation> findAllActive() {
        return reservationRepository.findAllActive(TimeProvider.now());
    }

    @Transactional
    public List<Reservation> findAllExpired() {
        return reservationRepository.findAllExpired(TimeProvider.now());
    }
}
