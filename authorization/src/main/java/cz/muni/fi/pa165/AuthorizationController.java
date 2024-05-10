package cz.muni.fi.pa165;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Martin Kuba
 * Taken from seminar-security
 */
@RestController
@RequestMapping(path = "/api", produces = MediaType.TEXT_PLAIN_VALUE)
@OpenAPIDefinition(
        info = @Info(title = "Authorization API", version = "1.0", description = "API secured by OAuth 2"),
        servers = @Server(description = "resource server", url = "http://localhost:8090")
)
@Tag(name = "AuthorizationService", description = "Authorization API for providing the bearer token")
public class AuthorizationController {
    @Operation(summary = "Get bearer token",
            security = @SecurityRequirement(name = AuthorizationServer.SECURITY_SCHEME_NAME, scopes = {"test_read"}),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - see content for the bearer token"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - access token not provided or not valid", content = @Content()),
            }
    )
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/bearer")
    public String getBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bearer token not found in the request");
        }
    }
}
