package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.ReservationDAO;
import cz.muni.fi.pa165.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
