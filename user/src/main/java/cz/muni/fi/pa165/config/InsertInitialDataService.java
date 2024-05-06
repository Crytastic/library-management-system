package cz.muni.fi.pa165.config;

import com.google.common.hash.Hashing;
import cz.muni.fi.pa165.data.model.User;
import cz.muni.fi.pa165.data.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
@Transactional
public class InsertInitialDataService {
    private final UserRepository userRepository;

    @Autowired
    public InsertInitialDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void insertInitialData() {
        insertLibrarians();
        insertAdultsMembers();
        insertChildMembers();
    }

    private void insertLibrarians() {
        UserType userType = UserType.LIBRARIAN;

        String username1 = "Rita Holbová";
        String password1 = "rita-dita-HOLBOVA";
        String address1 = "Jilemnického 240/14";
        LocalDate birthDate1 = LocalDate.of(1993, 4, 3);
        User librarian1 = new User(username1, getPasswordHash(password1), userType, address1, birthDate1);

        String username2 = "Pavol Kršek";
        String password2 = "9876Pavlik5432";
        String address2 = "Do Sadov 62";
        LocalDate birthDate2 = LocalDate.of(1989, 12, 2);
        User librarian2 = new User(username2, getPasswordHash(password2), userType, address2, birthDate2);

        userRepository.save(librarian1);
        userRepository.save(librarian2);
    }

    private void insertAdultsMembers() {
        UserType userType = UserType.MEMBER;

        String username1 = "Diana Suranová";
        String password1 = "didi89";
        String address1 = "Bratislavská 1603/78";
        LocalDate birthDate1 = LocalDate.of(2000, 12, 11);
        User member1 = new User(username1, getPasswordHash(password1), userType, address1, birthDate1);

        String username2 = "Roman Hruška";
        String password2 = "sarka2312dorka0708";
        String address2 = "Pionierska 80/44";
        LocalDate birthDate2 = LocalDate.of(1970, 7, 13);
        User member2 = new User(username2, getPasswordHash(password2), userType, address2, birthDate2);

        userRepository.save(member1);
        userRepository.save(member2);
    }

    private void insertChildMembers() {
        UserType userType = UserType.MEMBER;

        String username1 = "Martinka";
        String password1 = "Zajacova33";
        String address1 = "Koleslavová 1123/77";
        LocalDate birthDate1 = LocalDate.of(2015, 4, 4);
        User member1 = new User(username1, getPasswordHash(password1), userType, address1, birthDate1);

        String username2 = "filip20";
        String password2 = "0pilif0";
        String address2 = "Športová 32";
        LocalDate birthDate2 = LocalDate.of(2009, 9, 21);
        User member2 = new User(username2, getPasswordHash(password2), userType, address2, birthDate2);

        userRepository.save(member1);
        userRepository.save(member2);
    }

    private String getPasswordHash(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}
