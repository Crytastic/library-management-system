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

import static java.lang.String.format;

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

        System.out.println("Get rid off borrowed books.");
        removeBorrowedBooks(wantedBooks);
        System.out.println("Founded books except borrowed books: " + String.join(", ", wantedBooks.values()));

        System.out.println("If exists at least one available book, borrow it.");
        String userId = user.get("id").toString().replace('\"', ' ').strip();
        if (!wantedBooks.isEmpty()) {
            System.out.println("Check if the user is adult and can borrow book by himself.");
            if (userIsAdult(userId)) {
                System.out.println("The user is adult.");
                JsonNode borrowing = borrowFirstBook(wantedBooks, userId);
                String bookId = borrowing.get("bookId").toString().replace('\"', ' ').strip();
                String title = getTitleOfBorrowedBook(bookId);
                System.out.println("Book with title " + title + " borrowed.");

                System.out.println("Show the actual fine for user " + username +
                        " on book with title " + title + ". (0€ is expected)");
                String borrowingId = borrowing.get("id").toString().replace('\"', ' ').strip();
                String fine = getFineForBorrowing(borrowingId);
                System.out.println("The actual fine for book " + title + " is " + fine + "€.");
            } else {
                System.out.println("The user is not adult so he cannot borrow a book by himself.");
            }
        }
        System.out.println("Available books after borrowing: " + String.join(", ", wantedBooks.values()));
    }

    private static String getFineForBorrowing(String borrowingId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> borrowingResponse = restTemplate.exchange(
                format("http://localhost:8080/api/borrowings/%s/fine", borrowingId),
                HttpMethod.GET,
                null,
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        String fine = null;
        try {
            JsonNode fineInfo = om.readTree(borrowingResponse.getBody());
            fine = fineInfo.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return fine;
    }

    private static boolean userIsAdult(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> adultUsersResponse = restTemplate.exchange(
                "http://localhost:8082/api/users/adults",
                HttpMethod.GET,
                null,
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        List<String> adultUsersIds = new ArrayList<>();
        try {
            JsonNode adultUserInfo = om.readTree(adultUsersResponse.getBody());
            for (JsonNode jsonNode : adultUserInfo) {
                adultUsersIds.add(jsonNode.get("id").toString().replace('\"', ' ').strip());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return adultUsersIds.contains(userId);
    }

    private static String getTitleOfBorrowedBook(String bookId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> bookResponse = restTemplate.exchange(
                format("http://localhost:8083/api/books/%s", bookId),
                HttpMethod.GET,
                null,
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        String title = null;
        try {
            JsonNode book = om.readTree(bookResponse.getBody());
            title = book.get("title").toString().replace('\"', ' ').strip();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return title;
    }

    private static JsonNode borrowFirstBook(Map<String, String> wantedBooks, String userId) {
        String firstBookId = wantedBooks.keySet().iterator().next();
        wantedBooks.remove(firstBookId);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> borrowingResponse = restTemplate.exchange(
                format("http://localhost:8080/api/borrowings?bookId=%s&borrowerId=%s", firstBookId, userId),
                HttpMethod.POST,
                null,
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        JsonNode borrowing = null;
        try {
            borrowing = om.readTree(borrowingResponse.getBody());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return borrowing;
    }

    private static void removeBorrowedBooks(Map<String, String> wantedBooks) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> borrowingResponse = restTemplate.exchange(
                "http://localhost:8080/api/activeBorrowings",
                HttpMethod.GET,
                null,
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        List<String> borrowedBookIds = new ArrayList<>();
        try {
            JsonNode reservationInfo = om.readTree(borrowingResponse.getBody());
            for (JsonNode jsonNode : reservationInfo) {
                borrowedBookIds.add(jsonNode.get("bookId").toString().replace('\"', ' ').strip());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for (String reservationsId : borrowedBookIds) {
            wantedBooks.remove(reservationsId);
        }
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
                format("http://localhost:8083/api/books?author=%s", wantedAuthor),
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
        String username = "NovyUser";
        String password = "DeniskaMach9";
        String address = "Bohunice 450/6";
        LocalDate birthDate = LocalDate.of(1999, 9, 3);
        String userType = "MEMBER";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> userResponse = restTemplate.exchange(
                format("http://localhost:8082/api/users?username=%s&password=%s&address=%s&birthDate=%s&userType=%s",
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
