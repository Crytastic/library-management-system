package cz.muni.fi.pa165.util;

import cz.muni.fi.pa165.data.model.UserDAO;
import org.openapitools.model.UserType;

import java.time.LocalDate;

public class TestDataFactory {
    public static UserDAO firstMemberDAO = getUserDAOFactory(
            1L, "programmer123", "firstMemberDAOPasswordHash", UserType.MEMBER, "Botanická 68a, Brno", LocalDate.parse("2000-12-20"));

    public static UserDAO secondMemberDAO = getUserDAOFactory(
            2L, "mathematician123", "secondMemberDAOPasswordHash", UserType.MEMBER, "Lidická 12, Brno", LocalDate.parse("2002-12-22"));

    public static UserDAO firstLibrarianDAO = getUserDAOFactory(
            3L, "admin1", "admin1PasswordHash", UserType.LIBRARIAN, "Šumavská 10, Brno", LocalDate.parse("1999-12-19"));

    private static UserDAO getUserDAOFactory(Long id, String username, String passwordHash, UserType userType, String address, LocalDate birhDate) {
        return new UserDAO(id, username, passwordHash, userType, address, birhDate);
    }
}
