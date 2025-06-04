package unsa.sistemas.identityservice.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Authentication request payload with user credentials, Include a X-Tenant-Id in the headers")
public class AuthRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 5926468583005150707L;

    @Schema(
            description = "Email address",
            example = "jhon@doe.com"
    )
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @Schema(
            description = "User's password",
            example = "MySecretPass123"
    )
    @NotBlank(message = "Password is required")
    private String password;
}
