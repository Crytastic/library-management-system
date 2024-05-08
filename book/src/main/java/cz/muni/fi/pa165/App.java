package cz.muni.fi.pa165;

import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(x -> x
                        .requestMatchers(HttpMethod.GET, "/api/books").hasAuthority("SCOPE_test_read")
                        .requestMatchers(HttpMethod.POST, "/api/books").hasAuthority("SCOPE_test_write")
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()))
        ;
        return http.build();
    }

    @Bean
    public OpenApiCustomizer openAPICustomizer() {
        return openApi -> openApi.getComponents()
                .addSecuritySchemes("MUNI",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("get access token with OAuth 2 Authorization Code Grant")
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl("https://oidc.muni.cz/oidc/authorize")
                                                .tokenUrl("https://oidc.muni.cz/oidc/token")
                                                .scopes(new Scopes()
                                                        .addString("test_read", "reading events")
                                                        .addString("test_write", "creating events")
                                                )
                                        )
                                )
                );
    }
}
