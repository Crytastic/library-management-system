package cz.muni.fi.pa165.util;

import org.openapitools.model.BookDTO;
import org.openapitools.model.BookStatus;

public class BookDTOFactory {

    public static BookDTO createBook(String title, String description, String author, BookStatus status) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(title);
        bookDTO.setDescription(description);
        bookDTO.setAuthor(author);
        bookDTO.setStatus(status);
        return bookDTO;
    }
}
