package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.ReservationDAO;
import cz.muni.fi.pa165.repository.ReservationRepository;
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

    public List<ReservationDAO> findAll() {
        return reservationRepository.findAll();
    }

    public ReservationDAO createReservation(String book, String reservedBy) {
        ReservationDAO reservationDAO = new ReservationDAO(book, reservedBy, OffsetDateTime.now(), getDefaultReservationCancelDate());
        return reservationRepository.store(reservationDAO);
    }

    private OffsetDateTime getDefaultReservationCancelDate() {
        return OffsetDateTime.now().plusDays(3);
    }

    public Optional<ReservationDAO> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public Optional<ReservationDAO> updateById(Long id, String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        Optional<ReservationDAO> optionalReservation = reservationRepository.findById(id);

        if (optionalReservation.isEmpty()) {
            return Optional.empty();
        }

        ReservationDAO reservationDAO = optionalReservation.get();

        if (book != null) reservationDAO.setBook(book);
        if (reservedBy != null) reservationDAO.setReservedBy(reservedBy);
        if (reservedFrom != null) reservationDAO.setReservedFrom(reservedFrom);
        if (reservedTo != null) reservationDAO.setReservedTo(reservedTo);

        return reservationRepository.updateById(id, reservationDAO);

    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationDAO> findAllActive() {
        return reservationRepository.findAllActive();
    }

    public List<ReservationDAO> findAllExpired() {
        return reservationRepository.findAllExpired();
    }
}
