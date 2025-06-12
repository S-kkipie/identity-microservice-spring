package unsa.sistemas.identityservice.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO for registering a database connection for an organization")
public class RegisterDataBaseDTO {
    public static final String DBNAME = "_inventory_db";

    @Schema(
            description = "Database password",
            example = "MySecretPass123"
    )
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(
            description = "Database username (normally the user's ID)",
            example = "12312-1231-1212-3-213"
    )
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(
            description = "Unique organization code used as subdomain prefix. " +
                    "This will be used as the subdomain in a URL (e.g., 'my-code' in 'my-code.example.com'). " +
                    "Only letters, numbers and hyphens are allowed. Must not start or end with a hyphen.",
            example = "my-code"
    )
    @NotBlank(message = "Organization code is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?$",
            message = "orgCode must be a valid subdomain (e.g., 'my-code' in my-code.example.com)"
    )
    private String orgCode;


    @Schema(
            description = "Database connection URL",
            example = "jdbc:postgresql://localhost:5432/unsa_identity_db"
    )
    @NotBlank(message = "URL is required")
    private String url;
}
