import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

public class Script {
    public static void main(String[] args) {
        System.out.println("New user creates an member account.");
        JsonNode user = createMemberUser();
        String username = user.get("username").toString().replace('\"', ' ').strip();
        System.out.println("Account with username " + username + " created.");
    }

    private static JsonNode createMemberUser() {
        String username = "Danko";
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
