package cz.muni.fi.pa165.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ReservationSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/reservations/**").hasAuthority("SCOPE_read")
                        .requestMatchers(HttpMethod.DELETE, "/api/reservations/**").hasAuthority("SCOPE_test1")
                        .requestMatchers(HttpMethod.PATCH, "/api/reservations/**").hasAuthority("SCOPE_write")
                        .requestMatchers(HttpMethod.POST, "/api/reservations/**").hasAuthority("SCOPE_write")
                        .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()));

        return http.build();
    }
}
