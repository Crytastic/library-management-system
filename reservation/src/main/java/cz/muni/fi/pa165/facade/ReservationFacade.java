package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.mappers.ReservationMapper;
import cz.muni.fi.pa165.service.ReservationService;
import org.openapitools.model.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Facade layer for managing book reservations.
 * Provides methods for interacting with reservations.
 *
 * @author Martin Suchánek
 */
@Service
public class ReservationFacade {
    ReservationService reservationService;

    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationFacade(ReservationService reservationService, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    public List<ReservationDTO> findAll() {
        return reservationMapper.mapToList(reservationService.findAll());
    }

    public ReservationDTO createReservation(Long bookId, Long reserveeId) {
        return reservationMapper.mapToDto(reservationService.createReservation(bookId, reserveeId));
    }

    public ReservationDTO findById(Long id) {
        return reservationMapper.mapToDto(reservationService.findById(id));
    }

    public ReservationDTO updateById(Long id, Long bookId, Long reserveeId, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        return reservationMapper.mapToDto(reservationService.updateById(id, bookId, reserveeId, reservedFrom, reservedTo));
    }

    public void deleteById(Long id) {
        reservationService.deleteById(id);
    }

    public List<ReservationDTO> findAllActive() {
        return reservationMapper.mapToList(reservationService.findAllActive());
    }

    public List<ReservationDTO> findAllExpired() {
        return reservationMapper.mapToList(reservationService.findAllExpired());
    }

    public void deleteAll() {
        reservationService.deleteAll();
    }
}
