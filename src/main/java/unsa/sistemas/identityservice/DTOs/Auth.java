package unsa.sistemas.identityservice.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Authentication response containing access and refresh tokens")
public class Auth implements Serializable {

    @Serial
    private static final long serialVersionUID = -8091879091924046844L;

    @Schema(
            description = "JWT access token used to authenticate API requests",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String accessToken;

    @Schema(
            description = "Refresh token used to obtain a new access token",
            example = "dGhpc19pc19hX3JlZnJlc2hfdG9rZW4uLi4="
    )
    private String refresh;
}
