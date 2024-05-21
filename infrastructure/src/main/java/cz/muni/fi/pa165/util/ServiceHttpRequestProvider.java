package cz.muni.fi.pa165.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static java.lang.String.format;

@Service
public class ServiceHttpRequestProvider {
    public ResponseEntity<String> callGetBookById(Long id) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();


        return restTemplate.exchange(
                format("http://book:8083/api/books/%d", id),
                HttpMethod.GET,
                createRequestEntity(request.getHeader("Authorization")),
                String.class
        );
    }

    public ResponseEntity<String> callGetUserById(Long id) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        return restTemplate.exchange(
                format("http://user:8082/api/users/%d", id),
                HttpMethod.GET,
                createRequestEntity(request.getHeader("Authorization")),
                String.class
        );
    }

    private static HttpEntity<String> createRequestEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);
        return new HttpEntity<>(headers);
    }
}
