package cz.muni.fi.pa165.security;

import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private static final String SECURITY_SCHEME_OAUTH2 = "MUNI";
    private static final String SECURITY_SCHEME_BEARER = "Bearer";
    public static final String SECURITY_SCHEME_NAME = SECURITY_SCHEME_OAUTH2;

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

    @Bean
    public OpenApiCustomizer openAPICustomizer() {
        return openApi -> {
            log.info("adding security to OpenAPI description");
            openApi.getComponents()

                    .addSecuritySchemes(SECURITY_SCHEME_OAUTH2,
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
                                                            .addString("test_1", "deleting events")
                                                    )
                                            )
                                    )
                    )
            ;
        };
    }
}
