package com.example.recipea.controller;

import com.example.recipea.service.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Mahdi Sharifi
 */
public interface UserController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Issues token",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "403", description = "If use and password not match.", content = @Content)
    })
    @Operation(summary = "Return a JWT token into body of response and Authorization header.")
    ResponseEntity<UserDto> login(
            @Parameter(description = "Username for issuing token.", example = "mahdi")
            @RequestParam(value = "username") String username,
            @Parameter(description = "User's password. Not required now. Leave it blank")
            @RequestParam(required = false, value = "password") String password);
}
