package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.RentalDAO;
import cz.muni.fi.pa165.repository.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {
    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void findById_rentalFound_returnsRental() {
        Mockito.when(rentalRepository.findById(1L)).thenReturn(Optional.ofNullable(TestDataFactory.activeRentalDAO));
        Optional<RentalDAO> foundDao = rentalService.findById(1L);

        assertThat(foundDao).isPresent();
        assertThat(foundDao.get()).isEqualTo(TestDataFactory.activeRentalDAO);
    }

    @Test
    void findById_rentalNotFound_returnsEmptyOptional() {
        Mockito.when(rentalRepository.findById(11L)).thenReturn(Optional.empty());
        Optional<RentalDAO> foundDao = rentalService.findById(11L);

        assertThat(foundDao).isEmpty();
    }




}