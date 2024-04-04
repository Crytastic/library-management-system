package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.ReservationDAO;
import cz.muni.fi.pa165.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

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

    public ReservationDAO createRental(String book, String reservedBy) {
        ReservationDAO reservationDAO = new ReservationDAO(book, reservedBy, OffsetDateTime.now(), getDefaultReservationCancelDate());
        return reservationRepository.store(reservationDAO);
    }

    private OffsetDateTime getDefaultReservationCancelDate() {
        return OffsetDateTime.now().plusDays(3);
    }
}
