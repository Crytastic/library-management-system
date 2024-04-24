package cz.muni.fi.pa165.mappers;

import cz.muni.fi.pa165.data.model.Book;
import org.mapstruct.Mapper;
import org.openapitools.model.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO mapToDto(Book book);

    List<BookDTO> mapToList(List<Book> books);

    default Page<BookDTO> mapToPageDto(Page<Book> books) {
        return new PageImpl<>(mapToList(books.getContent()), books.getPageable(), books.getTotalPages());
    }
}
