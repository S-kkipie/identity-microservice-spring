package unsa.sistemas.identityservice.Docs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import unsa.sistemas.identityservice.Models.AbstractUser;
import unsa.sistemas.identityservice.Utils.ResponseWrapper;

@AllArgsConstructor
@Schema(description = "Contains a User in the response")
public class UserResponse extends ResponseWrapper<AbstractUser> {
}
