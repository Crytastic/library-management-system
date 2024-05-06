package cz.muni.fi.pa165.config;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.data.repository.ReservationRepository;
import cz.muni.fi.pa165.util.TimeProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@Transactional
public class InsertInitialDataService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public InsertInitialDataService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostConstruct
    public void insertInitialData() {
        reservationRepository.save(bookId3ReservedById5());
        reservationRepository.save(bookId6ReservedById1());
    }

    private Reservation bookId3ReservedById5() {
        Long bookId = 3L;
        Long reserveeId = 5L;
        OffsetDateTime reservedFrom = TimeProvider.now();
        OffsetDateTime reservedTo = TimeProvider.now().plusDays(3);

        return new Reservation(bookId, reserveeId, reservedFrom, reservedTo);
    }

    private Reservation bookId6ReservedById1() {
        Long bookId = 6L;
        Long reserveeId = 1L;
        OffsetDateTime reservedFrom = TimeProvider.now().minusDays(1);
        OffsetDateTime reservedTo = TimeProvider.now().plusDays(5);

        return new Reservation(bookId, reserveeId, reservedFrom, reservedTo);
    }
}
