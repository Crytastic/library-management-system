package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.data.repository.JpaReservationRepository;
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

    JpaReservationRepository jpaReservationRepository;

    @Autowired
    public ReservationService(JpaReservationRepository jpaReservationRepository) {
        this.jpaReservationRepository = jpaReservationRepository;
    }

    @Transactional
    public List<Reservation> findAll() {
        return jpaReservationRepository.findAll();
    }

    @Transactional
    public Reservation createReservation(String book, String reservedBy) {
        return jpaReservationRepository.save(new Reservation(book, reservedBy, TimeProvider.now(), getDefaultReservationCancelDate()));
    }

    private OffsetDateTime getDefaultReservationCancelDate() {
        return TimeProvider.now().plusDays(3);
    }

    @Transactional
    public Optional<Reservation> findById(Long id) {
        return jpaReservationRepository.findById(id);
    }

    @Transactional
    public int updateById(Long id, String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        return jpaReservationRepository.updateById(id, book, reservedBy, reservedFrom, reservedTo);

    }

    @Transactional
    public void deleteById(Long id) {
        jpaReservationRepository.deleteById(id);
    }

    @Transactional
    public List<Reservation> findAllActive() {
        return jpaReservationRepository.findAllActive(TimeProvider.now());
    }

    @Transactional
    public List<Reservation> findAllExpired() {
        return jpaReservationRepository.findAllExpired(TimeProvider.now());
    }
}
