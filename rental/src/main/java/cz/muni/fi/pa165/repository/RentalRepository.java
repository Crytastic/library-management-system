package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.data.model.Rental;
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
    private final HashMap<Long, Rental> rentals = new HashMap<>();
    private static Long index = 1L;

    public List<Rental> findAll() {
        return rentals.values().stream().toList();
    }

    public Rental store(Rental rental) {
        rental.setId(index);
        rentals.put(rental.getId(), rental);
        index++;
        return rental;
    }

    public Optional<Rental> findById(Long id) {
        return Optional.ofNullable(rentals.get(id));
    }

    public boolean deleteById(Long id) {
        return rentals.remove(id) != null;
    }

    public Optional<Rental> updateById(Long id, Rental rental) {
        rentals.put(id, rental);
        return Optional.ofNullable(rental);
    }
}
