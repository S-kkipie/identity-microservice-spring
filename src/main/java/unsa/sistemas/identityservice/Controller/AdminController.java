package unsa.sistemas.identityservice.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsa.sistemas.identityservice.DTOs.AbstractUserDTO;
import unsa.sistemas.identityservice.Models.Principal.PrincipalUser;
import unsa.sistemas.identityservice.Services.PrincipalUserService;
import unsa.sistemas.identityservice.Utils.ResponseHandler;
import unsa.sistemas.identityservice.Utils.ResponseWrapper;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/principal-users")
public class AdminController {
    private final PrincipalUserService principalUserService;

    @Operation(summary = "Get all principal users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<ResponseWrapper<Object>> getAllUsers() {
        List<PrincipalUser> users = principalUserService.getAllUsers();
        return ResponseHandler.generateResponse("Users fetched", HttpStatus.OK, users);
    }

    @Operation(summary = "Get principal user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Object>> getUserById(@PathVariable String id) {
        try {
            PrincipalUser user = principalUserService.loadUserById(id);
            return ResponseHandler.generateResponse("User found", HttpStatus.OK, user);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("User not found", HttpStatus.NOT_FOUND, null);
        }
    }


    @Operation(summary = "Update a principal user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Object>> updateUser(@PathVariable String id, @Valid @RequestBody AbstractUserDTO dto) {
        try {
            PrincipalUser user = principalUserService.updateUser(id, dto);
            return ResponseHandler.generateResponse("User updated", HttpStatus.OK, user);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("User not found", HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Delete a principal user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Object>> deleteUser(@PathVariable String id) {
        try {
            principalUserService.deleteUser(id);
            return ResponseHandler.generateResponse("User deleted", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("User not found", HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
