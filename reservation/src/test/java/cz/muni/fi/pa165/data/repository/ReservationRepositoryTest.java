package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Long activeReservationId;

    private Long expiredReservationId;
    private OffsetDateTime activeReservationReservedFrom;

    private OffsetDateTime activeReservationReservedTo;

    private OffsetDateTime expiredReservationReservedFrom;

    private OffsetDateTime expiredReservationReservedTo;

    @BeforeEach
    void setUp() {
        activeReservationReservedFrom = TimeProvider.now().minusMinutes(10);
        activeReservationReservedTo = TimeProvider.now().plusDays(3);
        expiredReservationReservedFrom = TimeProvider.now().minusMinutes(10).minusDays(4);
        expiredReservationReservedTo = TimeProvider.now().minusDays(1);
        Reservation activeReservation = new Reservation(1L, 1L, activeReservationReservedFrom, activeReservationReservedTo);
        Reservation expiredReservation = new Reservation(2L, 2L, expiredReservationReservedFrom, expiredReservationReservedTo);

        Reservation persistedActiveReservation = testEntityManager.persist(activeReservation);
        Reservation persistedExpredReservation = testEntityManager.persist(expiredReservation);

        activeReservationId = persistedActiveReservation.getId();
        expiredReservationId = persistedExpredReservation.getId();
    }

    @Test
    void updateById_idFound_returnsOneOrMore() {
        int updatedCount = reservationRepository.updateById(activeReservationId, 1L, null, null, null);
        assertThat(updatedCount).isEqualTo(1);
    }

    @Test
    void updateById_idNotFound_returnsZero() {
        // activeReservationId + expiredReservationId is here to prevent random match of id that should not be found
        int updatedCount = reservationRepository.updateById(activeReservationId + expiredReservationId, 1L, null, null, null);
        assertThat(updatedCount).isEqualTo(0);
    }

    @Test
    void findAllActive_activeReservationExists_returnsListWithSizeOne() {
        List<Reservation> activeReservations = reservationRepository.findAllActive(TimeProvider.now());
        assertThat(activeReservations).hasSize(1);
        Reservation reservation = activeReservations.getFirst();
        assertThat(reservation.getBookId()).isEqualTo(1L);
        assertThat(reservation.getReserveeId()).isEqualTo(1L);
        assertThat(reservation.getReservedFrom()).isEqualTo(activeReservationReservedFrom);
        assertThat(reservation.getReservedTo()).isEqualTo(activeReservationReservedTo);
    }

    @Test
    void findAllExpired_expiredReservationExists_returnsListWithSizeOne() {
        List<Reservation> activeReservations = reservationRepository.findAllExpired(TimeProvider.now());
        assertThat(activeReservations).hasSize(1);
        Reservation reservation = activeReservations.getFirst();
        assertThat(reservation.getBookId()).isEqualTo(2L);
        assertThat(reservation.getReserveeId()).isEqualTo(2L);
        assertThat(reservation.getReservedFrom()).isEqualTo(expiredReservationReservedFrom);
        assertThat(reservation.getReservedTo()).isEqualTo(expiredReservationReservedTo);
    }
}