package cz.muni.fi.pa165.util;

import org.openapitools.model.ReservationDTO;

import java.time.OffsetDateTime;

/**
 * @author Maxmilián Šeffer
 */
public class ReservationDTOFactory {
    public static ReservationDTO createReservation(Long id,String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(id);
        reservationDTO.setBook(book);
        reservationDTO.setReservedBy(reservedBy);
        reservationDTO.setReservedFrom(reservedFrom);
        reservationDTO.setReservedTo(reservedTo);
        return reservationDTO;
    }
}
