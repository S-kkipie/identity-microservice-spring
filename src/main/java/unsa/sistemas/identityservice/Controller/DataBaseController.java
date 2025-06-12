package unsa.sistemas.identityservice.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unsa.sistemas.identityservice.DTOs.RegisterDataBaseDTO;
import unsa.sistemas.identityservice.Docs.BadResponse;
import unsa.sistemas.identityservice.Docs.StringResponse;
import unsa.sistemas.identityservice.Services.RegisterDataBaseService;
import unsa.sistemas.identityservice.Utils.ResponseHandler;
import unsa.sistemas.identityservice.Utils.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.sql.SQLException;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class DataBaseController {

    private final RegisterDataBaseService registerDataBaseService;

    @Operation(summary = "Register a new database without existing schema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully created a new database and connection",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StringResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Duplicate key error (database name or identifier already used)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BadResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "SQL Error or internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BadResponse.class)
                    )
            )
    })
    @PostMapping("/register-new")
    public ResponseEntity<ResponseWrapper<Object>> createDataSourceWithoutSchemaInitialize(
            @Valid @RequestBody RegisterDataBaseDTO dataBaseDTO) {
        try {
            return ResponseHandler.generateResponse(
                    "Successfully created a new connection with new database",
                    HttpStatus.OK,
                    registerDataBaseService.registerNewDataBase(dataBaseDTO)
            );
        } catch (SQLException e) {
            return ResponseHandler.generateResponse("SQL Error", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (DuplicateKeyException e) {
            return ResponseHandler.generateResponse("Duplicate key", HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Operation(summary = "Register an existing database with schema initialization")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully connected to an existing database",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StringResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Duplicate key error (already registered)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BadResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "SQL Error or internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BadResponse.class)
                    )
            )
    })
    @PostMapping("/register-existing")
    public ResponseEntity<ResponseWrapper<Object>> createDataSourceWithSchemaInitialize(
            @Valid @RequestBody RegisterDataBaseDTO dataBaseDTO) {
        try {
            return ResponseHandler.generateResponse(
                    "Successfully created a new connection with existing database",
                    HttpStatus.OK,
                    registerDataBaseService.registerExistingDataBase(dataBaseDTO)
            );
        } catch (SQLException e) {
            return ResponseHandler.generateResponse("SQL Error", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (DuplicateKeyException e) {
            return ResponseHandler.generateResponse("Duplicate key", HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
