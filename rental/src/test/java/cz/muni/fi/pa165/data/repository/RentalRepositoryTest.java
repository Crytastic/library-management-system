package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Rental;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Long rentalId;

    @BeforeEach
    void setUp() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Rental rental = new Rental(
                "Active test book",
                "Rental creator",
                borrowDate,
                expectedReturnDate,
                false,
                null,
                new BigDecimal(1),
                false);

        Rental persistedRental = testEntityManager.persist(rental);
        rentalId = persistedRental.getId();
    }

    @Test
    void updateById_idFound_returnsOneOrMore() {
        int updatedCount = rentalRepository.updateById(rentalId, "New book name", null, null, null, null, null, null, null);
        assertThat(updatedCount).isEqualTo(1);
    }

    @Test
    void updateById_idNotFound_returnZero() {
        int updatedCount = rentalRepository.updateById(rentalId - 1, "New book name", null, null, null, null, null, null, null);
        assertThat(updatedCount).isEqualTo(0);
    }
}