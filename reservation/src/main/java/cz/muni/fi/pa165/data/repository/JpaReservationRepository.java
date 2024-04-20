package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {
}
