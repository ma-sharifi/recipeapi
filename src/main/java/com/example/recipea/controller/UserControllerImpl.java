package com.example.recipea.controller;

import com.example.recipea.security.JwtTokenUtil;
import com.example.recipea.service.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mahdi Sharifi
 */

@RestController
@RequestMapping("v1/users")
@Tag(name = "User-controller for issuing JWT token", description = "Issue token based on username")
@Slf4j
public class UserControllerImpl implements UserController {

    /**
     * Issue token base on username
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> login(
            @RequestParam(value = "username") String username, @RequestParam(required = false, value = "password") String password) {
        log.debug("#UserController login method called.");
        // Assumed we checked the user and password and found they are a match. then we issued a token.
        String token = JwtTokenUtil.issueJwtToken(username);
        return ResponseEntity.ok().header("Authorization", token).body(new UserDto(username, token));
    }

}
