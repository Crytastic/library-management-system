package cz.muni.fi.pa165.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAuthority("SCOPE_test_read")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasAuthority("SCOPE_test_1")
                        .requestMatchers(HttpMethod.PATCH, "/api/**").hasAuthority("SCOPE_test_write")
                        .requestMatchers(HttpMethod.POST, "/api/**").hasAuthority("SCOPE_test_write")
                        .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()));
        return http.build();
    }
}

