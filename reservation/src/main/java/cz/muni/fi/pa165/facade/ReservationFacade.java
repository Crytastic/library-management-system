package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.dao.ReservationDAO;
import cz.muni.fi.pa165.service.ReservationService;
import org.openapitools.model.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private ReservationDTO convertToDTO(ReservationDAO reservationDAO) {
        return new ReservationDTO()
                .book(reservationDAO.getBook())
                .reservedBy(reservationDAO.getReservedBy())
                .reservedFrom(reservationDAO.getReservedFrom())
                .reservedTo(reservationDAO.getReservedTo());
    }

    public ReservationDTO createRental(String book, String reservedBy) {
        ReservationDAO rentalDAO = reservationService.createRental(book, reservedBy);
        return convertToDTO(rentalDAO);
    }

    public Optional<ReservationDTO> findById(Long id) {
        return reservationService.findById(id).map(this::convertToDTO);
    }
}
