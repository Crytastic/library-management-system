package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.service.RentalService;
import cz.muni.fi.pa165.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.RentalDTO;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RentalFacadeTest {
    @Mock
    RentalService rentalService;

    @InjectMocks
    RentalFacade rentalFacade;

    @Test
    void findById_userFound_returnsUser() {
        Mockito.when(rentalService.findById(2L)).thenReturn(Optional.ofNullable(TestDataFactory.activeRentalDAO));

        Optional<RentalDTO> rentalDTO = rentalFacade.findById(2L);

        assertThat(rentalDTO).isPresent();
        assertThat(rentalDTO.get()).isEqualTo(TestDataFactory.activeRentalDTO);
    }

    @Test
    void findById_userNotFound_returnsUser() {
        Mockito.when(rentalService.findById(11L)).thenReturn(Optional.empty());

        Optional<RentalDTO> rentalDTO = rentalFacade.findById(11L);

        assertThat(rentalDTO).isEmpty();
    }

}