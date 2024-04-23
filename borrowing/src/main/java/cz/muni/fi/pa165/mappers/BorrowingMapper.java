package cz.muni.fi.pa165.mappers;

import cz.muni.fi.pa165.data.model.Borrowing;
import org.mapstruct.Mapper;
import org.openapitools.model.BorrowingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowingMapper {

    BorrowingDTO mapToDto(Borrowing borrowing);

    List<BorrowingDTO> mapToList(List<Borrowing> borrowings);

    default Page<BorrowingDTO> mapToPageDto(Page<Borrowing> borrowings) {
        return new PageImpl<>(mapToList(borrowings.getContent()), borrowings.getPageable(), borrowings.getTotalPages());
    }
}