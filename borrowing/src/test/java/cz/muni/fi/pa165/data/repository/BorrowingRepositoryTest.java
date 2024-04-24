package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Borrowing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BorrowingRepositoryTest {
    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Long borrowingId;

    @BeforeEach
    void setUp() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Borrowing borrowing = new Borrowing(
                1L,
                1L,
                borrowDate,
                expectedReturnDate,
                false,
                null,
                new BigDecimal(1),
                false);

        Borrowing persistedBorrowing = testEntityManager.persist(borrowing);
        borrowingId = persistedBorrowing.getId();
    }

    @Test
    void updateById_idFound_returnsOneOrMore() {
        int updatedCount = borrowingRepository.updateById(borrowingId, 1L, null, null, null, null, null, null, null);
        assertThat(updatedCount).isEqualTo(1);
    }

    @Test
    void updateById_idNotFound_returnZero() {
        int updatedCount = borrowingRepository.updateById(borrowingId - 1, 1L, null, null, null, null, null, null, null);
        assertThat(updatedCount).isEqualTo(0);
    }
}
