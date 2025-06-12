package unsa.sistemas.identityservice.Docs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import unsa.sistemas.identityservice.Utils.ResponseWrapper;

@AllArgsConstructor
@Schema(description = "Returns a description")
public class StringResponse extends ResponseWrapper<String> {
}
