package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.mappers.ReservationMapper;
import cz.muni.fi.pa165.service.ReservationService;
import org.openapitools.model.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

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

    public ReservationDTO createReservation(String book, String reservedBy) {
        return reservationMapper.mapToDto(reservationService.createReservation(book, reservedBy));
    }

    public Optional<ReservationDTO> findById(Long id) {
        return reservationService.findById(id).map(reservationMapper::mapToDto);
    }

    public int updateById(Long id, String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        return reservationService.updateById(id, book, reservedBy, reservedFrom, reservedTo);
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
}
