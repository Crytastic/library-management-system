package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRentalRepository extends JpaRepository<Rental, Long> {

}