package unsa.sistemas.identityservice.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "User registration request payload")
public class RegisterRequest {
    @Schema(
            description = "User's email address. Must be a valid email format.",
            example = "user@example.com"
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String username;

    @Schema(
            description = "User's password. Must be at least 8 characters long.",
            example = "SecurePass123"
    )
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;
}
