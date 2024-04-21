package cz.muni.fi.pa165.mappers;

import cz.muni.fi.pa165.data.model.Reservation;
import org.mapstruct.Mapper;

import org.openapitools.model.ReservationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationDTO mapToDto(Reservation reservation);

    List<ReservationDTO> mapToList(List<Reservation> reservations);

    default Page<ReservationDTO> mapToPageDto(Page<Reservation> reservations) {
        return new PageImpl<>(mapToList(reservations.getContent()), reservations.getPageable(), reservations.getTotalPages());
    }
}