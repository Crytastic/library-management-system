package cz.muni.fi.pa165.mappers;

import cz.muni.fi.pa165.data.model.User;
import org.mapstruct.Mapper;
import org.openapitools.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO mapToDto(User user);

    List<UserDTO> mapToList(List<User> users);

    default Page<UserDTO> mapToPageDto(Page<User> users) {
        return new PageImpl<>(mapToList(users.getContent()), users.getPageable(), users.getTotalPages());
    }
}