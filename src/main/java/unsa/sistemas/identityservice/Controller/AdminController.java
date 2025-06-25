package unsa.sistemas.identityservice.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsa.sistemas.identityservice.DTOs.AbstractUserDTO;
import unsa.sistemas.identityservice.Models.Role;
import unsa.sistemas.identityservice.Models.Tenant.User;
import unsa.sistemas.identityservice.Services.UserService;
import unsa.sistemas.identityservice.Utils.ResponseHandler;

import unsa.sistemas.identityservice.Utils.ResponseWrapper;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/users")
public class AdminController {
    private final UserService userService;

    @Operation(summary = "Get a list of users", parameters = {
            @Parameter(name = "page", description = "Page number for pagination", required = true, in = ParameterIn.QUERY, example = "0")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invalid pagination parameters",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<ResponseWrapper<Page<User>>> getAllUsers(@RequestParam(defaultValue = "0") int page) {
        Page<User> users = userService.getAllUsers(page);
        return ResponseHandler.generateResponse("Users fetched", HttpStatus.OK, users);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Object>> getUserById(@PathVariable String id) {
        try {
            User user = userService.loadUserById(id);
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
            User user = userService.updateUser(id, dto);
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
            userService.deleteUser(id);
            return ResponseHandler.generateResponse("User deleted", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("User not found", HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Create a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input or user already exists", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<ResponseWrapper<Object>> createUser(@Valid @RequestBody AbstractUserDTO userDTO) {
        try {
            //TODO: CREATE USER WITH ROLE FROM REQUEST OR NOT
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRole());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setCountry(userDTO.getCountry());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setImageUrl(userDTO.getImageUrl());
            user.setRole(Role.ROLE_EMPLOYEE);
            User createdUser = userService.createUser(user);
            return ResponseHandler.generateResponse("User created", HttpStatus.CREATED, createdUser);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}
