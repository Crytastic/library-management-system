package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.service.ReservationService;
import org.openapitools.model.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Facade layer for managing book reservations.
 * Provides methods for interacting with reservations.
 *
 * @author Martin Suchánek
 */
@Service
public class ReservationFacade {

    ReservationService reservationService;

    @Autowired
    public ReservationFacade(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public List<ReservationDTO> findAll() {
        return reservationService
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO()
                .id(reservation.getId())
                .book(reservation.getBook())
                .reservedBy(reservation.getReservedBy())
                .reservedFrom(reservation.getReservedFrom())
                .reservedTo(reservation.getReservedTo());
    }

    public ReservationDTO createReservation(String book, String reservedBy) {
        Reservation reservation = reservationService.createReservation(book, reservedBy);
        return convertToDTO(reservation);
    }

    public Optional<ReservationDTO> findById(Long id) {
        return reservationService.findById(id).map(this::convertToDTO);
    }

    public Optional<ReservationDTO> updateById(Long id, String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        return reservationService.updateById(id, book, reservedBy, reservedFrom, reservedTo).map(this::convertToDTO);
    }

    public void deleteById(Long id) {
        reservationService.deleteById(id);
    }

    public List<ReservationDTO> findAllActive() {
        return reservationService
                .findAllActive()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> findAllExpired() {
        return reservationService
                .findAllExpired()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
