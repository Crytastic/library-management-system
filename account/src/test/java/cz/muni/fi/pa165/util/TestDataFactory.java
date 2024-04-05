package cz.muni.fi.pa165.util;

import com.google.common.hash.Hashing;
import cz.muni.fi.pa165.data.model.UserDAO;
import org.openapitools.model.UserType;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class TestDataFactory {
    public static String firstMemberDAOPassword = "firstMemberDAOPasswordHash";

    public static String secondMemberDAOPassword = "secondMemberDAOPasswordHash";

    public static String firstLibrarianDAOPassword = "admin1PasswordHash";

    public static UserDAO firstMemberDAO = getUserDAOFactory(
            1L, "programmer123", createPasswordHash(firstMemberDAOPassword), UserType.MEMBER, "Botanická 68a, Brno", LocalDate.parse("2000-12-20"));

    public static UserDAO secondMemberDAO = getUserDAOFactory(
            2L, "mathematician123", createPasswordHash(secondMemberDAOPassword), UserType.MEMBER, "Lidická 12, Brno", LocalDate.parse("2002-12-22"));

    public static UserDAO firstLibrarianDAO = getUserDAOFactory(
            3L, "admin1", createPasswordHash(firstLibrarianDAOPassword), UserType.LIBRARIAN, "Šumavská 10, Brno", LocalDate.parse("1999-12-19"));

    private static UserDAO getUserDAOFactory(Long id, String username, String passwordHash, UserType userType, String address, LocalDate birhDate) {
        return new UserDAO(id, username, passwordHash, userType, address, birhDate);
    }

    public static String createPasswordHash(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}
