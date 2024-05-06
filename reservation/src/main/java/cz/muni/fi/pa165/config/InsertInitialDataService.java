package cz.muni.fi.pa165.config;

import cz.muni.fi.pa165.data.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InsertInitialDataService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public InsertInitialDataService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
}
