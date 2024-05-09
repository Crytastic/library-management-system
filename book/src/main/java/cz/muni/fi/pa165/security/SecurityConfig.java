package cz.muni.fi.pa165.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@OpenAPIDefinition
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(x -> x
                        .requestMatchers(HttpMethod.GET, "/api/books").hasAuthority("SCOPE_test_read")
                        .requestMatchers(HttpMethod.POST, "/api/books").hasAuthority("SCOPE_test_write")
                        .requestMatchers(HttpMethod.DELETE, "/api/books").hasAuthority("SCOPE_test_1")
                        .requestMatchers(HttpMethod.GET, "/api/books/{id}").hasAuthority("SCOPE_test_read")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/{id}").hasAuthority("SCOPE_test_1")
                        .requestMatchers(HttpMethod.PATCH, "/api/books/{id}").hasAuthority("SCOPE_test_write")
                        .requestMatchers(HttpMethod.GET, "/api/books/{id}/borrowings").hasAuthority("SCOPE_test_read")
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()))
        ;
        return http.build();
    }
}
