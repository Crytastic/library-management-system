package cz.muni.fi.pa165.util;

import com.google.common.hash.Hashing;
import cz.muni.fi.pa165.data.model.User;
import org.openapitools.model.UserType;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * @author Martin Suchánek
 */
public class TestDataFactory {
    public static String firstMemberDAOPassword = "firstMemberDAOPasswordHash";

    public static String secondMemberDAOPassword = "secondMemberDAOPasswordHash";

    public static String firstLibrarianDAOPassword = "admin1PasswordHash";

    public static User firstMemberDAO = getUserDAOFactory(
            1L, "programmer123", createPasswordHash(firstMemberDAOPassword), UserType.MEMBER, "Botanická 68a, Brno", LocalDate.parse("2000-12-20"));

    public static User secondMemberDAO = getUserDAOFactory(
            2L, "mathematician123", createPasswordHash(secondMemberDAOPassword), UserType.MEMBER, "Lidická 12, Brno", LocalDate.parse("2002-12-22"));

    public static User firstLibrarianDAO = getUserDAOFactory(
            3L, "admin1", createPasswordHash(firstLibrarianDAOPassword), UserType.LIBRARIAN, "Šumavská 10, Brno", LocalDate.parse("1999-12-19"));

    private static User getUserDAOFactory(Long id, String username, String passwordHash, UserType userType, String address, LocalDate birhDate) {
        User user = new User(username, passwordHash, userType, address, birhDate);
        user.setId(id);
        return user;
    }

    public static String createPasswordHash(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}
