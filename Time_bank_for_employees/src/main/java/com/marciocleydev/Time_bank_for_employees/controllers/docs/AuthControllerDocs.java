package com.marciocleydev.Time_bank_for_employees.controllers.docs;

import com.marciocleydev.Time_bank_for_employees.DTO.security.AccountCredentialsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthControllerDocs {
    @Operation(summary = "Authenticate and generate a token",
            description = "Authenticate a user and generate a token by username and password",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "Success",responseCode = "200",content = @Content),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<?> signin(AccountCredentialsDTO credentials);

    @Operation(summary = "Returns a new token by refreshToken",
            description = "Refresh token for authenticated user and returns a token ",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "Success",responseCode = "200",content = @Content),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<?> refreshToken(String username, String refreshToken);
}

