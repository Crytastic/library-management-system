package cz.muni.fi.pa165.mappers;

import cz.muni.fi.pa165.data.model.Rental;
import org.mapstruct.Mapper;
import org.openapitools.model.RentalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    RentalDTO mapToDto(Rental rental);

    List<RentalDTO> mapToList(List<Rental> rentals);

    default Page<RentalDTO> mapToPageDto(Page<Rental> rentals) {
        return new PageImpl<>(mapToList(rentals.getContent()), rentals.getPageable(), rentals.getTotalPages());
    }
}