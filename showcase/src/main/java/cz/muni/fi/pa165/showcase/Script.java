package cz.muni.fi.pa165.showcase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

import static java.lang.String.format;

//For multiple executing the script do not forget to change name of user, otherwise exception will be thrown.
public class Script {

    private static String token;

    private static final String banner = """
            ███████╗██╗  ██╗ ██████╗ ██╗    ██╗ ██████╗ █████╗ ███████╗███████╗
            ██╔════╝██║  ██║██╔═══██╗██║    ██║██╔════╝██╔══██╗██╔════╝██╔════╝
            ███████╗███████║██║   ██║██║ █╗ ██║██║     ███████║███████╗█████╗ \s
            ╚════██║██╔══██║██║   ██║██║███╗██║██║     ██╔══██║╚════██║██╔══╝ \s
            ███████║██║  ██║╚██████╔╝╚███╔███╔╝╚██████╗██║  ██║███████║███████╗
            ╚══════╝╚═╝  ╚═╝ ╚═════╝  ╚══╝╚══╝  ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝
            """;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Access token was not provided. Try: 'java -jar /path/to/script/jar.jar <token>'");
            return;
        }

        token = args[0];

        try {
            System.out.println("\n");
            System.out.println(banner);
            System.out.println("Not registered user creates an member account:");
            String name = "Denisa Machova_" + generateRandomDigitSequence(5);
            String password = "DeniskaMach9";
            JsonNode user = createMemberUser(name, password);
            String username = user.get("username").toString().replace('\"', ' ').strip();
            printLoadingDots(2);
            System.out.println("Account with username '" + username + "' created.");
            printJsonNodeRecursively(user, "   ");

            System.out.println("\n");
            String wantedAuthor = "Dan Brown";
            System.out.println("User wants to find all books from author '" + wantedAuthor + "':");
            printLoadingDots(2);
            Map<String, String> wantedBooks = findAllBooksByAuthor(wantedAuthor);
            System.out.println("Number of founded books by '" + wantedAuthor + "' is " + wantedBooks.size() + "  :  " + String.join(", ", wantedBooks.values()));

            System.out.println("\n");
            System.out.println("Get rid off reserved books:");
            printLoadingDots(2);
            removeReservedBooks(wantedBooks);
            System.out.println("Number of founded books by '" + wantedAuthor + "' which are not reserved is " + wantedBooks.size() + "  :  " + String.join(", ", wantedBooks.values()));

            System.out.println("\n");
            System.out.println("Get rid off borrowed books.");
            printLoadingDots(2);
            removeBorrowedBooks(wantedBooks);
            System.out.println("Number of founded books by '" + wantedAuthor + "' which are not reserved and not borrowed is " + wantedBooks.size() + "  :  " + String.join(", ", wantedBooks.values()));

            System.out.println("\n");
            System.out.println("If exists at least one available book, borrow it:");
            printLoadingDots(2);
            String userId = user.get("id").toString().replace('\"', ' ').strip();
            if (!wantedBooks.isEmpty()) {
                System.out.println("Yes");
                System.out.println("Check if the user is adult and can borrow book by himself:");
                printLoadingDots(2);
                if (userIsAdult(userId)) {
                    System.out.println("The user is adult.");
                    System.out.print("\n\n");
                    System.out.println("Borrowing book:");
                    printLoadingDots(3);
                    JsonNode borrowing = borrowFirstBook(wantedBooks, userId);
                    String bookId = borrowing.get("bookId").toString().replace('\"', ' ').strip();
                    String title = getTitleOfBook(bookId);
                    System.out.println("Book with title " + title + " borrowed.");
                    printJsonNodeRecursively(borrowing, "   ");

                    System.out.println("\n");
                    System.out.println("Show the actual fine for user " + username +
                            " on book with title " + title + ". (0 eur is expected)");
                    printLoadingDots(1);
                    String borrowingId = borrowing.get("id").toString().replace('\"', ' ').strip();
                    String fine = getFineForBorrowing(borrowingId);
                    System.out.println("The actual fine for book " + title + " is " + fine + " eur");
                } else {
                    System.out.println("The user is not adult so he cannot borrow a book by himself.");
                    System.exit(0);
                }
            } else {
                System.out.println("All books from author" + wantedAuthor + " are reserved or borrowed.");
                System.out.println("Sorry :(");
                System.exit(0);
            }

            System.out.println("\n");
            System.out.println("Now, if still exists at least one available book, reserve it.");
            printLoadingDots(2);
            System.out.println("Number of available books after borrowing is " + wantedBooks.size() + "  :  " + String.join(", ", wantedBooks.values()));

            if (!wantedBooks.isEmpty()) {

                System.out.println("\n");
                System.out.println("Reserving book:");
                printLoadingDots(2);
                JsonNode reservation = reserveFirstBook(wantedBooks, userId);
                String bookId = reservation.get("bookId").toString().replace('\"', ' ').strip();
                String title = getTitleOfBook(bookId);
                System.out.println("Book with title " + title + " reserved.");
                printJsonNodeRecursively(reservation, "   ");
            } else {
                System.out.println("All books from author" + wantedAuthor + " are reserved or borrowed.");
                System.out.println("Sorry :(");
                System.exit(0);
            }

            System.out.println();
            System.out.println("Feel free to check created user, borrowing and reservation via swagger :)");

        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Unauthorized. Token is not valid");
            System.exit(1);

        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("Forbidden. Provided access token does not have enough access rights");
            System.exit(2);

        } catch (JsonProcessingException e) {
            System.out.println("Unable to process output");
            System.exit(3);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(4);
        }
    }


    private static JsonNode reserveFirstBook(Map<String, String> wantedBooks, String userId) throws JsonProcessingException {
        String firstBookId = wantedBooks.keySet().iterator().next();
        wantedBooks.remove(firstBookId);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> reservationResponse = restTemplate.exchange(
                format("http://localhost:8081/api/reservations?bookId=%s&reserveeId=%s", firstBookId, userId),
                HttpMethod.POST,
                createRequestEntity(),
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        return om.readTree(reservationResponse.getBody());
    }

    private static String getFineForBorrowing(String borrowingId) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> borrowingResponse = restTemplate.exchange(
                format("http://localhost:8080/api/borrowings/%s/fine", borrowingId),
                HttpMethod.GET,
                createRequestEntity(),
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        JsonNode fineInfo = om.readTree(borrowingResponse.getBody());
        return fineInfo.toString();
    }

    private static boolean userIsAdult(String userId) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> adultUsersResponse = restTemplate.exchange(
                "http://localhost:8082/api/users/adults",
                HttpMethod.GET,
                createRequestEntity(),
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        List<String> adultUsersIds = new ArrayList<>();

        JsonNode adultUserInfo = om.readTree(adultUsersResponse.getBody());
        for (JsonNode jsonNode : adultUserInfo) {
            adultUsersIds.add(jsonNode.get("id").toString().replace('\"', ' ').strip());
        }
        return adultUsersIds.contains(userId);
    }

    private static String getTitleOfBook(String bookId) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> bookResponse = restTemplate.exchange(
                format("http://localhost:8083/api/books/%s", bookId),
                HttpMethod.GET,
                createRequestEntity(),
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        JsonNode book = om.readTree(bookResponse.getBody());
        return book.get("title").toString().replace('\"', ' ').strip();
    }

    private static JsonNode borrowFirstBook(Map<String, String> wantedBooks, String userId) throws JsonProcessingException {
        String firstBookId = wantedBooks.keySet().iterator().next();
        wantedBooks.remove(firstBookId);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> borrowingResponse = restTemplate.exchange(
                format("http://localhost:8080/api/borrowings?bookId=%s&borrowerId=%s", firstBookId, userId),
                HttpMethod.POST,
                createRequestEntity(),
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        return om.readTree(borrowingResponse.getBody());
    }

    private static void removeBorrowedBooks(Map<String, String> wantedBooks) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> borrowingResponse = restTemplate.exchange(
                "http://localhost:8080/api/activeBorrowings",
                HttpMethod.GET,
                createRequestEntity(),
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        List<String> borrowedBookIds = new ArrayList<>();

        JsonNode reservationInfo = om.readTree(borrowingResponse.getBody());
        for (JsonNode jsonNode : reservationInfo) {
            borrowedBookIds.add(jsonNode.get("bookId").toString().replace('\"', ' ').strip());
        }

        for (String reservationsId : borrowedBookIds) {
            wantedBooks.remove(reservationsId);
        }
    }

    private static void removeReservedBooks(Map<String, String> wantedBooks) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> reservationResponse = restTemplate.exchange(
                "http://localhost:8081/api/reservations/active",
                HttpMethod.GET,
                createRequestEntity(),
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        List<String> reservationBookIds = new ArrayList<>();

        JsonNode reservationInfo = om.readTree(reservationResponse.getBody());
        for (JsonNode jsonNode : reservationInfo) {
            reservationBookIds.add(jsonNode.get("bookId").toString().replace('\"', ' ').strip());
        }

        for (String reservationsId : reservationBookIds) {
            wantedBooks.remove(reservationsId);
        }
    }

    private static Map<String, String> findAllBooksByAuthor(String wantedAuthor) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> booksResponse = restTemplate.exchange(
                format("http://localhost:8083/api/books?author=%s", wantedAuthor),
                HttpMethod.GET,
                createRequestEntity(),
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        Map<String, String> books = new HashMap<>();
        JsonNode booksInfo = om.readTree(booksResponse.getBody());
        for (JsonNode jsonNode : booksInfo) {
            String titleOfBook = jsonNode.get("title").toString().replace('\"', ' ').strip();
            String idOfBook = jsonNode.get("id").toString().replace('\"', ' ').strip();
            books.put(idOfBook, titleOfBook);
        }
        return books;
    }

    private static JsonNode createMemberUser(String username, String password) throws JsonProcessingException {
        String address = "Bohunice 450/6";
        LocalDate birthDate = LocalDate.of(1999, 9, 3);
        String userType = "MEMBER";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> userResponse = restTemplate.exchange(
                format("http://localhost:8082/api/users?username=%s&password=%s&address=%s&birthDate=%s&userType=%s",
                        username, password, address, birthDate, userType),
                HttpMethod.POST,
                createRequestEntity(),
                String.class
        );

        ObjectMapper om = new ObjectMapper();
        return om.readTree(userResponse.getBody());
    }

    private static HttpEntity<String> createRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    public static String generateRandomDigitSequence(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Append random digit (0-9)
        }

        return sb.toString();
    }

    private static void printJsonNodeRecursively(JsonNode jsonNode, String indent) {
        jsonNode.fields().forEachRemaining(entry -> {
            System.out.println(indent + entry.getKey() + ": " + entry.getValue());
            if (entry.getValue().isObject()) {
                printJsonNodeRecursively(entry.getValue(), indent + "  ");
            }
        });
    }

    public static void printLoadingDots(int numOfCycles) {
        System.out.print("Loading ");

        for (int i = 0; i < numOfCycles; i++) {
            for (int y = 0; y < 3; y++) {
                System.out.print('.');
                try {
                    Thread.sleep(500); // Adjust the delay time as needed
                } catch (InterruptedException ignored) {

                }
            }

            System.out.print("\b\b\b   \b\b\b");
        }
        System.out.print("\b\b\b\b\b\b\b\b         \b\b\b\b\b\b\b\b");
    }
}
