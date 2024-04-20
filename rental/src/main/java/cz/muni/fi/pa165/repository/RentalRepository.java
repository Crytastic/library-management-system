package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.data.RentalDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Repository layer for managing rentals.
 * Provides methods for storing, retrieving and updating, rentals.
 *
 * @author Maxmilán Šeffer
 */
@Repository
public class RentalRepository {
    private final HashMap<Long, RentalDAO> rentals = new HashMap<>();
    private static Long index = 1L;

    public List<RentalDAO> findAll() {
        return rentals.values().stream().toList();
    }

    public RentalDAO store(RentalDAO rentalDAO) {
        rentalDAO.setId(index);
        rentals.put(rentalDAO.getId(), rentalDAO);
        index++;
        return rentalDAO;
    }

    public Optional<RentalDAO> findById(Long id) {
        return Optional.ofNullable(rentals.get(id));
    }

    public boolean deleteById(Long id) {
        return rentals.remove(id) != null;
    }

    public Optional<RentalDAO> updateById(Long id, RentalDAO rentalDAO) {
        rentals.put(id, rentalDAO);
        return Optional.ofNullable(rentalDAO);
    }
}
