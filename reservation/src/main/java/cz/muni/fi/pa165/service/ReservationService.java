package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.data.repository.ReservationRepository;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

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
    public Reservation createReservation(Long bookId, Long reserveeId) {
        return reservationRepository.save(new Reservation(bookId, reserveeId, TimeProvider.now(), getDefaultReservationCancelDate()));
    }

    private OffsetDateTime getDefaultReservationCancelDate() {
        return TimeProvider.now().plusDays(3);
    }

    @Transactional
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Reservation with id: %d not found", id)));
    }

    @Transactional
    public int updateById(Long id, Long bookId, Long reserveeId, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        int updatedCount = reservationRepository.updateById(id, bookId, reserveeId, reservedFrom, reservedTo);
        if (updatedCount > 0) {
            return updatedCount;
        } else {
            throw new ResourceNotFoundException(String.format("Reservation with id: %d not found", id));
        }

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
