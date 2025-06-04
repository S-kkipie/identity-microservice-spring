package unsa.sistemas.identityservice.Docs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import unsa.sistemas.identityservice.Utils.ResponseWrapper;

@AllArgsConstructor
@Schema(description = "Response returned when an error occurs")
public class BadResponse extends ResponseWrapper<String> {
}
