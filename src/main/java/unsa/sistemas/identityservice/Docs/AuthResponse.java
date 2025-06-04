package unsa.sistemas.identityservice.Docs;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import unsa.sistemas.identityservice.DTOs.Auth;
import unsa.sistemas.identityservice.Utils.ResponseWrapper;

@AllArgsConstructor
@Schema(description = "Response with the JWT token")
public class AuthResponse extends ResponseWrapper<Auth> {
}
