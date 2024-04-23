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

    @BeforeEach
    void setUp() {
        User user = new User(
                "username",
                "passwordHash",
                UserType.MEMBER,
                "Address",
                LocalDate.parse("2000-02-02")
        );
        testEntityManager.persist(user);
    }

    @Test
    void findAllByUserType_someUserFound_returnsListWithSizeOne() {
        List<User> users = userRepository.findAllByUserType(UserType.MEMBER);
        assertThat(users).hasSize(1);

        User user = users.getFirst();
        assertThat(user.getUserType()).isEqualTo(UserType.MEMBER);
        assertThat(user.getAddress()).isEqualTo("Address");
        assertThat(user.getPasswordHash()).isEqualTo("passwordHash");
        assertThat(user.getUsername()).isEqualTo("username");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.parse("2000-02-02"));

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
        assertThat(user.getUserType()).isEqualTo(UserType.MEMBER);
        assertThat(user.getAddress()).isEqualTo("Address");
        assertThat(user.getPasswordHash()).isEqualTo("passwordHash");
        assertThat(user.getUsername()).isEqualTo("username");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.parse("2000-02-02"));
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
        assertThat(user.getUserType()).isEqualTo(UserType.MEMBER);
        assertThat(user.getAddress()).isEqualTo("Address");
        assertThat(user.getPasswordHash()).isEqualTo("passwordHash");
        assertThat(user.getUsername()).isEqualTo("username");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.parse("2000-02-02"));
    }

    @Test
    void findUserByUsername_userNotFound_returnEmptyList() {
        User user = userRepository.findUserByUsername("NotExistingUsername");

        assertThat(user).isNull();
    }
}