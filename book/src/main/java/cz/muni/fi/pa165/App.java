package cz.muni.fi.pa165;

import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@SpringBootApplication
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final String SECURITY_SCHEME_OAUTH2 = "MUNI";
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(x -> x
                        .requestMatchers(HttpMethod.GET, "/api/books").hasAuthority("SCOPE_book_read")
                        .requestMatchers(HttpMethod.POST, "/api/books").hasAuthority("SCOPE_book_write")
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(Customizer.withDefaults()))
        ;
        return http.build();
    }

    /**
     * Add security definitions to generated openapi.yaml.
     */
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
                                                            .addString("book_read", "reading events")
                                                            .addString("book_write", "creating events")
                                                    )
                                            )
                                    )
                    )
                    /*.addSecuritySchemes(SECURITY_SCHEME_BEARER,
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .description("provide a valid access token")
                    )*/
            ;
        };
    }

    /**
     * Display a hint in the log.
     */
    @EventListener
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        log.info("**************************");
        int port = event.getWebServer().getPort();
        log.info("visit http://localhost:{}/swagger-ui.html for UI", port);
        log.info("visit http://localhost:{}/openapi.yaml for OpenAPI document", port);
        log.info("**************************");
    }
}
