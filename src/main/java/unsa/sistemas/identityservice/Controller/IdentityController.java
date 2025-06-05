package unsa.sistemas.identityservice.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import unsa.sistemas.identityservice.DTOs.Auth;
import unsa.sistemas.identityservice.DTOs.AuthRequest;
import unsa.sistemas.identityservice.DTOs.RegisterRequest;
import unsa.sistemas.identityservice.Docs.AuthResponse;
import unsa.sistemas.identityservice.Docs.BadResponse;
import unsa.sistemas.identityservice.Docs.UserResponse;
import unsa.sistemas.identityservice.Services.ComposeUserDetailService;
import unsa.sistemas.identityservice.Utils.JWTUtil;
import unsa.sistemas.identityservice.Utils.ResponseHandler;
import unsa.sistemas.identityservice.Utils.ResponseWrapper;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("user")
public class IdentityController {
    private final ComposeUserDetailService userService;
    private final JWTUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;


    @Operation(summary = "Auth a user")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authentication successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class)
                            )),
                    @ApiResponse(
                            responseCode = "401",
                            description = "User authentication failed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BadResponse.class)
                            )
                    )
            }
    )
    @PostMapping("login")
    public ResponseEntity<ResponseWrapper<Object>> createAuthenticationToken(@Valid @RequestBody AuthRequest authenticationRequest) {

        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Authentication Failed", HttpStatus.UNAUTHORIZED,
                    "Invalid credentials, please check details and try again");
        }

        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        return ResponseHandler.generateResponse("Authentication successfully", HttpStatus.OK,
                new Auth(token, refreshToken));
    }

    @Operation(summary = "Register a user")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registration successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User registration failed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BadResponse.class)
                            )
                    )
            }
    )
    @PostMapping("register")
    public ResponseEntity<ResponseWrapper<Object>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserDetails newUser = userService.createUser(request);

            log.debug("New user: {}", newUser.getUsername());

            final String token = jwtTokenUtil.generateToken(newUser);

            final String refreshToken = jwtTokenUtil.generateRefreshToken(newUser);

            return ResponseHandler.generateResponse("User created successfully", HttpStatus.CREATED,
                    new Auth(token, refreshToken));

        } catch (IllegalStateException e) {
            log.error(e.getMessage());
            return ResponseHandler.generateResponse("Failed to register a user", HttpStatus.BAD_REQUEST,
                    "The username is already taken");

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseHandler.generateResponse("Failed to register a user", HttpStatus.BAD_REQUEST,
                    "A error occurred while trying to register a user");
        }
    }

    @Operation(summary = "Retrieves a user's profile")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User profile generated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User profile generation failed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BadResponse.class)
                            )
                    )
            }
    )
    @GetMapping("profile")
    public ResponseEntity<ResponseWrapper<Object>> retrieveUserProfile() {
        try {
            return ResponseHandler.generateResponse("User Profile generated", HttpStatus.OK, userService.findCurrentUser());
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Failed to retrieve a user profile", HttpStatus.BAD_REQUEST,
                    "No profile found");
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            Authentication authenticationRequest =
                    new UsernamePasswordAuthenticationToken(username, password);

            Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

            SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED: " + e.getMessage(), e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("AUTHENTICATION_FAILED: " + e.getMessage(), e);
        }
    }

}