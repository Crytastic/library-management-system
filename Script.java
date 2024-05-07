import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Script {
    public static void main(String[] args) {
        System.out.println("New user creates an member account.");
        JsonNode user = createMemberUser();
        String username = user.get("username").toString().replace('\"', ' ').strip();
        System.out.println("Account with username " + username + " created.");

        String wantedAuthor = "Dan Brown";
        System.out.println("User wants to find all books from " + wantedAuthor + ".");
        Map<String, String> wantedBooks = findAllBooksByAuthor(wantedAuthor);
        System.out.println("Founded books by " + wantedAuthor + ": " + String.join(", ", wantedBooks.values()));

        System.out.println("Get rid off reserved books.");
        removeReservedBooks(wantedBooks);
        System.out.println("Founded books except reserved books: " + String.join(", ", wantedBooks.values()));
    }

    private static void removeReservedBooks(Map<String, String> wantedBooks) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> reservationResponse = restTemplate.exchange(
                "http://localhost:8081/api/reservations/active",
                HttpMethod.GET,
                null,
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        List<String> reservationBookIds = new ArrayList<>();
        try {
            JsonNode reservationInfo = om.readTree(reservationResponse.getBody());
            for (JsonNode jsonNode : reservationInfo) {
                reservationBookIds.add(jsonNode.get("bookId").toString().replace('\"', ' ').strip());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for (String reservationsId : reservationBookIds) {
            wantedBooks.remove(reservationsId);
        }
    }

    private static Map<String, String> findAllBooksByAuthor(String wantedAuthor) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> booksResponse = restTemplate.exchange(
                String.format("http://localhost:8083/api/books?author=%s", wantedAuthor),
                HttpMethod.GET,
                null,
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        Map<String, String> books = new HashMap<>();
        try {
            JsonNode booksInfo = om.readTree(booksResponse.getBody());
            for (JsonNode jsonNode : booksInfo) {
                String titleOfBook = jsonNode.get("title").toString().replace('\"', ' ').strip();
                String idOfBook = jsonNode.get("id").toString().replace('\"', ' ').strip();
                books.put(idOfBook, titleOfBook);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return books;
    }

    private static JsonNode createMemberUser() {
        String username = "Danko2";
        String password = "DeniskaMach9";
        String address = "Bohunice 450/6";
        LocalDate birthDate = LocalDate.of(1999, 9, 3);
        String userType = "MEMBER";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> userResponse = restTemplate.exchange(
                String.format("http://localhost:8082/api/users?username=%s&password=%s&address=%s&birthDate=%s&userType=%s",
                        username, password, address, birthDate, userType),
                HttpMethod.POST,
                null,
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        JsonNode userInfo = null;
        try {
            userInfo = om.readTree(userResponse.getBody());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userInfo;
    }
}
