package cz.muni.fi.pa165.util;

import cz.muni.fi.pa165.data.model.UserDAO;
import org.openapitools.model.UserType;

import java.time.LocalDate;

public class TestDataFactory {
    public static UserDAO userDAO = getUserDAOFactory();

    private static UserDAO getUserDAOFactory() {
        return new UserDAO(1L, "programmer123", "passwordHash", UserType.MEMBER, "Botanická 68a, Brno", LocalDate.parse("2000-12-20"));
    }
}
