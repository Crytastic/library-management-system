package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private static final String USERNAME = "username";
    private static final String PASSWORD_HASH = "passwordHash";
    private static final UserType USER_TYPE = UserType.MEMBER;
    private static final String ADDRESS = "Address";
    private static final LocalDate BIRTH_DATE = LocalDate.parse("2000-02-02");


    @BeforeEach
    void setUp() {
        User user = new User(
                USERNAME,
                PASSWORD_HASH,
                USER_TYPE,
                ADDRESS,
                BIRTH_DATE
        );
        testEntityManager.persist(user);
    }

    @Test
    void findAllByUserType_someUserFound_returnsListWithSizeOne() {
        List<User> users = userRepository.findAllByUserType(UserType.MEMBER);
        assertThat(users).hasSize(1);

        User user = users.getFirst();
        assertThat(user.getUserType()).isEqualTo(USER_TYPE);
        assertThat(user.getAddress()).isEqualTo(ADDRESS);
        assertThat(user.getPasswordHash()).isEqualTo(PASSWORD_HASH);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getBirthDate()).isEqualTo(BIRTH_DATE);

    }

    @Test
    void findAllByUserType_userNotFound_returnEmptyList() {
        List<User> users = userRepository.findAllByUserType(UserType.LIBRARIAN);
        assertThat(users).hasSize(0);
    }

    @Test
    void findAllByAge_someUserFound_returnsListWithSizeOne() {
        List<User> users = userRepository.findAllByAge(LocalDate.parse("2000-02-02"));
        assertThat(users).hasSize(1);

        User user = users.getFirst();
        assertThat(user.getUserType()).isEqualTo(USER_TYPE);
        assertThat(user.getAddress()).isEqualTo(ADDRESS);
        assertThat(user.getPasswordHash()).isEqualTo(PASSWORD_HASH);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getBirthDate()).isEqualTo(BIRTH_DATE);
    }

    @Test
    void findAllByAge_userNotFound_returnEmptyList() {
        List<User> users = userRepository.findAllByAge(LocalDate.parse("1999-02-02"));
        assertThat(users).hasSize(0);
    }

    @Test
    void findUserByUsername_someUserFound_returnsListWithSizeOne() {
        User user = userRepository.findUserByUsername("username");

        assertThat(user).isNotNull();
        assertThat(user.getUserType()).isEqualTo(USER_TYPE);
        assertThat(user.getAddress()).isEqualTo(ADDRESS);
        assertThat(user.getPasswordHash()).isEqualTo(PASSWORD_HASH);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getBirthDate()).isEqualTo(BIRTH_DATE);
    }

    @Test
    void findUserByUsername_userNotFound_returnEmptyList() {
        User user = userRepository.findUserByUsername("NotExistingUsername");

        assertThat(user).isNull();
    }
}